package com.learpc.learpc.core.media

import org.junit.Assert.assertEquals
import org.junit.Test

class RadioReconnectPolicyTest {
    private val policy = RadioReconnectPolicy(
        maxAttempts = 4,
        baseDelayMs = 1_000L,
        maxDelayMs = 8_000L
    )

    @Test
    fun `returns exponential backoff for early attempts`() {
        assertEquals(1_000L, policy.nextRetryDelayMs(0))
        assertEquals(2_000L, policy.nextRetryDelayMs(1))
        assertEquals(4_000L, policy.nextRetryDelayMs(2))
    }

    @Test
    fun `caps delay at max delay`() {
        assertEquals(8_000L, policy.nextRetryDelayMs(3))
    }

    @Test
    fun `returns negative one after max attempts`() {
        assertEquals(-1L, policy.nextRetryDelayMs(4))
        assertEquals(-1L, policy.nextRetryDelayMs(5))
    }
}
