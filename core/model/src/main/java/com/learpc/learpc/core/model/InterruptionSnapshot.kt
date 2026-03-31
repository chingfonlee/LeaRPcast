package com.learpc.learpc.core.model

data class InterruptionSnapshot(
    val playableItem: PlayableItem? = null,
    val positionMs: Long = 0L,
    val isPlaying: Boolean = false,
    val wasUserPause: Boolean = false,
    val capturedAtEpochMs: Long = System.currentTimeMillis()
)
