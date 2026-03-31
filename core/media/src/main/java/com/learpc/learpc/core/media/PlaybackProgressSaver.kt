package com.learpc.learpc.core.media

interface PlaybackProgressSaver {
    suspend fun savePlaybackProgress(
        episodeId: String,
        positionMs: Long,
        isCompleted: Boolean
    )
}
