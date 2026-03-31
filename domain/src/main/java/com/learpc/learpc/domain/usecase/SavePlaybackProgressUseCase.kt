package com.learpc.learpc.domain.usecase

import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.core.media.PlaybackProgressSaver
import javax.inject.Inject

class SavePlaybackProgressUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) : PlaybackProgressSaver {
    override suspend fun savePlaybackProgress(
        episodeId: String,
        positionMs: Long,
        isCompleted: Boolean
    ) {
        episodeRepository.updateProgress(episodeId, positionMs, isCompleted)
    }

    suspend operator fun invoke(
        episodeId: String,
        positionMs: Long,
        isCompleted: Boolean
    ) {
        savePlaybackProgress(episodeId, positionMs, isCompleted)
    }
}
