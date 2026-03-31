package com.learpc.learpc.core.media

import androidx.media3.common.PlaybackException
import javax.inject.Inject

class PlaybackErrorClassifier @Inject constructor() {
    fun isRetryable(exception: PlaybackException): Boolean {
        return isRetryableErrorCode(exception.errorCode)
    }

    internal fun isRetryableErrorCode(errorCode: Int): Boolean {
        return when (errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> true

            // Review this mapping when bumping the Media3 version.
            else -> false
        }
    }
}
