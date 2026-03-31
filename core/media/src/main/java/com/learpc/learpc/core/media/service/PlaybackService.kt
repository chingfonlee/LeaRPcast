package com.learpc.learpc.core.media.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.learpc.learpc.core.datastore.preferences.UserPreferencesDataSource
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.media.PlaybackErrorClassifier
import com.learpc.learpc.core.media.PlaybackProgressSaver
import com.learpc.learpc.core.media.PlayerEventMapper
import com.learpc.learpc.core.media.RadioReconnectPolicy
import com.learpc.learpc.core.media.ResumeAfterInterruptionPolicy
import com.learpc.learpc.core.model.InterruptionSnapshot
import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.media.factory.MediaSessionFactory
import com.learpc.learpc.core.media.factory.PlayerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@UnstableApi
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    @Inject lateinit var playerFactory: PlayerFactory
    @Inject lateinit var mediaSessionFactory: MediaSessionFactory
    @Inject lateinit var playbackController: PlaybackController
    @Inject lateinit var playbackErrorClassifier: PlaybackErrorClassifier
    @Inject lateinit var radioReconnectPolicy: RadioReconnectPolicy
    @Inject lateinit var playerEventMapper: PlayerEventMapper
    @Inject lateinit var playbackProgressSaver: PlaybackProgressSaver
    @Inject lateinit var userPreferencesDataSource: UserPreferencesDataSource

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var serviceJob: Job? = null
    private var serviceScope: CoroutineScope? = null
    private var progressSaveJob: Job? = null
    private var reconnectJob: Job? = null
    private var retryAttempt = 0
    private var interruptionSnapshot: InterruptionSnapshot? = null
    private var lastPlayWhenReadyChangeReason: Int? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                resetReconnectState()
            } else if (playbackState == Player.STATE_ENDED) {
                saveProgress(isCompleted = true)
                clearInterruptionSnapshot()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (mediaItem != null) {
                resetReconnectState()
                clearInterruptionSnapshot()
            }
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            lastPlayWhenReadyChangeReason = reason

            if (!playWhenReady && reason == Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST) {
                clearInterruptionSnapshot()
                return
            }

            if (playWhenReady && reason == Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS) {
                maybeResumeAfterInterruption()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (!isPlaying) {
                saveProgress(isCompleted = false)
                maybeCaptureInterruptionSnapshot()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            handlePlayerError(error)
        }
    }

    override fun onCreate() {
        super.onCreate()
        serviceJob = SupervisorJob()
        serviceScope = CoroutineScope(Dispatchers.Main.immediate + requireNotNull(serviceJob))

        val localPlayer = playerFactory.create(this)
        player = localPlayer
        localPlayer.addListener(playerListener)
        mediaSession = mediaSessionFactory.create(this, localPlayer)
        progressSaveJob = serviceScope?.launch {
            while (true) {
                delay(PROGRESS_SAVE_INTERVAL_MS)
                if (localPlayer.isPlaying) {
                    saveProgress(isCompleted = false)
                }
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        progressSaveJob?.cancel()
        progressSaveJob = null
        reconnectJob?.cancel()
        reconnectJob = null
        serviceScope = null
        serviceJob?.cancel()
        serviceJob = null
        player?.removeListener(playerListener)
        mediaSession?.release()
        mediaSession = null
        player?.release()
        player = null
        super.onDestroy()
    }

    private fun handlePlayerError(error: PlaybackException) {
        val localPlayer = player ?: return
        val scope = serviceScope ?: return

        if (playbackErrorClassifier.isRetryable(error)) {
            if (retryAttempt >= radioReconnectPolicy.maxAttempts) {
                reconnectJob?.cancel()
                reconnectJob = null
                playbackController.updatePlaybackState(
                    PlaybackStateModel.Error(playerEventMapper.mapPlaybackError(error.errorCode))
                )
                return
            }

            val delayMs = radioReconnectPolicy.nextRetryDelayMs(retryAttempt)
            retryAttempt += 1
            playbackController.updatePlaybackState(PlaybackStateModel.Reconnecting)

            reconnectJob?.cancel()
            reconnectJob = scope.launch {
                delay(delayMs)
                localPlayer.prepare()
            }
        } else {
            reconnectJob?.cancel()
            reconnectJob = null
            playbackController.updatePlaybackState(
                PlaybackStateModel.Error(playerEventMapper.mapPlaybackError(error.errorCode))
            )
        }
    }

    private fun resetReconnectState() {
        retryAttempt = 0
        reconnectJob?.cancel()
        reconnectJob = null
    }

    private fun maybeCaptureInterruptionSnapshot() {
        val localPlayer = player ?: return
        if (lastPlayWhenReadyChangeReason != Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS) {
            return
        }

        interruptionSnapshot = InterruptionSnapshot(
            playableItem = localPlayer.currentMediaItem.toPlayableItem(),
            positionMs = localPlayer.currentPosition,
            isPlaying = false,
            wasUserPause = false,
            capturedAtEpochMs = System.currentTimeMillis()
        )
    }

    private fun maybeResumeAfterInterruption() {
        val localPlayer = player ?: return
        val scope = serviceScope ?: return
        val snapshot = interruptionSnapshot ?: return

        scope.launch {
            val settings = userPreferencesDataSource.observePreferences().first()
            if (ResumeAfterInterruptionPolicy.shouldResume(snapshot, settings)) {
                localPlayer.play()
                clearInterruptionSnapshot()
            }
        }
    }

    private fun clearInterruptionSnapshot() {
        interruptionSnapshot = null
    }

    private fun saveProgress(isCompleted: Boolean) {
        val localPlayer = player ?: return
        val scope = serviceScope ?: return
        val currentItemId = localPlayer.currentMediaItem?.mediaId?.takeIf { it.isNotBlank() } ?: return
        val positionMs = localPlayer.currentPosition

        scope.launch {
            playbackProgressSaver.savePlaybackProgress(
                episodeId = currentItemId,
                positionMs = positionMs,
                isCompleted = isCompleted
            )
        }
    }

    private fun MediaItem?.toPlayableItem(): PlayableItem? {
        val item = this ?: return null
        val mediaUri = item.localConfiguration?.uri?.toString()
            ?: item.requestMetadata.mediaUri?.toString()
            ?: item.mediaId

        val title = item.mediaMetadata.title?.toString()
            ?: item.mediaMetadata.displayTitle?.toString()
            ?: item.mediaId

        val subtitle = item.mediaMetadata.subtitle?.toString()
            ?: item.mediaMetadata.artist?.toString()
            ?: item.mediaMetadata.albumArtist?.toString()

        val imageUri = item.mediaMetadata.artworkUri?.toString()

        val id = item.mediaId.ifBlank { mediaUri.ifBlank { title } }

        return PlayableItem(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUri = imageUri,
            mediaUri = mediaUri
        )
    }

    private companion object {
        const val PROGRESS_SAVE_INTERVAL_MS = 30_000L
    }
}
