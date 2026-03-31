package com.learpc.learpc.core.media

import androidx.media3.common.PlaybackException
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaybackErrorClassifierTest {
    private val classifier = PlaybackErrorClassifier()

    @Test
    fun `network failed is retryable`() {
        assertTrue(
            classifier.isRetryableErrorCode(
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
            )
        )
    }

    @Test
    fun `network timeout is retryable`() {
        assertTrue(
            classifier.isRetryableErrorCode(
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT
            )
        )
    }

    @Test
    fun `bad http status is not retryable`() {
        assertFalse(
            classifier.isRetryableErrorCode(
                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
            )
        )
    }

    @Test
    fun `parsing error is not retryable`() {
        assertFalse(
            classifier.isRetryableErrorCode(
                PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED
            )
        )
    }
}
