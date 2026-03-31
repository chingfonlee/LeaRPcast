package com.learpc.learpc.core.media

import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import com.learpc.learpc.core.model.PlaybackError
import com.learpc.learpc.core.model.PlaybackStateModel
import javax.inject.Inject

class PlayerEventMapper @Inject constructor() {

    fun mapState(
        playbackState: Int,
        playWhenReady: Boolean,
        error: PlaybackException?
    ): PlaybackStateModel {
        error?.let { return PlaybackStateModel.Error(mapPlaybackError(it.errorCode)) }

        return when (playbackState) {
            Player.STATE_BUFFERING -> PlaybackStateModel.Buffering
            Player.STATE_READY -> {
                if (playWhenReady) {
                    PlaybackStateModel.Playing
                } else {
                    PlaybackStateModel.Paused
                }
            }
            Player.STATE_ENDED,
            Player.STATE_IDLE -> PlaybackStateModel.Idle
            else -> PlaybackStateModel.Idle
        }
    }

    internal fun mapPlaybackError(errorCode: Int): PlaybackError {
        return when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED -> PlaybackError.Network

            PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
            PlaybackException.ERROR_CODE_IO_NO_PERMISSION -> PlaybackError.SourceUnavailable

            PlaybackException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED,
            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_UNSUPPORTED,
            PlaybackException.ERROR_CODE_DECODING_FAILED -> PlaybackError.UnsupportedFormat

            else -> PlaybackError.Unknown
        }
    }
}
