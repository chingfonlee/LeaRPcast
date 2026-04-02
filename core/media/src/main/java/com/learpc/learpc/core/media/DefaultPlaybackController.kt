package com.learpc.learpc.core.media

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.session.MediaController
import android.os.Bundle
import android.net.Uri
import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.PlaybackStateModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DefaultPlaybackController @Inject constructor(
    private val playerEventMapper: PlayerEventMapper
) : PlaybackController {
    private val playbackStateMutable = MutableStateFlow<PlaybackStateModel>(PlaybackStateModel.Idle)
    private val currentItemMutable = MutableStateFlow<PlayableItem?>(null)

    private var mediaController: MediaController? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            syncPlaybackState()
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            syncPlaybackState()
        }

        override fun onPlayerError(error: PlaybackException) {
            syncPlaybackState()
        }

        override fun onPlayerErrorChanged(error: PlaybackException?) {
            syncPlaybackState()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            syncCurrentItem(mediaItem)
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            syncCurrentItem()
        }
    }

    override val playbackState: StateFlow<PlaybackStateModel> = playbackStateMutable.asStateFlow()
    override val currentItem: StateFlow<PlayableItem?> = currentItemMutable.asStateFlow()

    override fun connectController(mediaController: MediaController) {
        if (this.mediaController === mediaController) {
            syncFromController(mediaController)
            return
        }

        this.mediaController?.removeListener(playerListener)
        this.mediaController = mediaController
        mediaController.addListener(playerListener)
        syncFromController(mediaController)
    }

    override fun setItem(item: PlayableItem) {
        val controller = mediaController ?: return
        val extras = Bundle().apply {
            item.fallbackMediaUri?.takeIf { it.isNotBlank() }?.let {
                putString(PlayableItemMetadataKeys.FALLBACK_MEDIA_URI, it)
            }
        }
        val mediaItem = MediaItem.Builder()
            .setMediaId(item.id)
            .setUri(item.mediaUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(item.title)
                    .setSubtitle(item.subtitle)
                    .setArtworkUri(item.imageUri?.let(android.net.Uri::parse))
                    .apply {
                        if (!extras.isEmpty) {
                            setExtras(extras)
                        }
                    }
                    .build()
            )
            .build()

        currentItemMutable.value = item
        controller.setMediaItem(mediaItem)
        controller.prepare()
    }

    override fun updatePlaybackState(playbackState: PlaybackStateModel) {
        playbackStateMutable.value = playbackState
    }

    override fun play() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    private fun syncFromController(controller: MediaController) {
        playbackStateMutable.value = playerEventMapper.mapState(
            playbackState = controller.playbackState,
            playWhenReady = controller.playWhenReady,
            error = controller.playerError
        )
        syncCurrentItem(controller.currentMediaItem)
    }

    private fun syncPlaybackState() {
        val controller = mediaController
        if (controller == null) {
            playbackStateMutable.value = PlaybackStateModel.Idle
            return
        }

        playbackStateMutable.value = playerEventMapper.mapState(
            playbackState = controller.playbackState,
            playWhenReady = controller.playWhenReady,
            error = controller.playerError
        )
    }

    private fun syncCurrentItem(mediaItem: MediaItem? = mediaController?.currentMediaItem) {
        currentItemMutable.value = mediaItem.toPlayableItem()
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
        val fallbackMediaUri = item.mediaMetadata.extras?.getString(
            PlayableItemMetadataKeys.FALLBACK_MEDIA_URI
        )

        val id = item.mediaId.ifBlank { mediaUri.ifBlank { title } }

        return PlayableItem(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUri = imageUri,
            mediaUri = mediaUri,
            fallbackMediaUri = fallbackMediaUri
        )
    }
}
