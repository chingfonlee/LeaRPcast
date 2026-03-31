package com.learpc.learpc.core.media

data class RadioReconnectPolicy(
    val maxAttempts: Int,
    val baseDelayMs: Long,
    val maxDelayMs: Long
) {
    fun nextRetryDelayMs(attempt: Int): Long {
        if (attempt >= maxAttempts) return -1L

        val multiplier = 1L shl attempt.coerceAtLeast(0).coerceAtMost(62)
        val delayMs = baseDelayMs * multiplier
        return delayMs.coerceAtMost(maxDelayMs)
    }
}
