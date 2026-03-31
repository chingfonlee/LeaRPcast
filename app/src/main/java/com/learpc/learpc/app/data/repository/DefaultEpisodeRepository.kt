package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.source.EpisodeLocalDataSource
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultEpisodeRepository @Inject constructor(
    private val episodeLocalDataSource: EpisodeLocalDataSource
) : EpisodeRepository {
    override fun observeByPodcastId(podcastId: String): Flow<List<Episode>> {
        return episodeLocalDataSource.observeByPodcastId(podcastId)
    }

    override fun observeAll(): Flow<List<Episode>> {
        return episodeLocalDataSource.observeAll()
    }

    override suspend fun getById(id: String): Episode? {
        return episodeLocalDataSource.getById(id)
    }

    override suspend fun upsert(episode: Episode) {
        episodeLocalDataSource.upsert(episode)
    }

    override suspend fun updateProgress(
        episodeId: String,
        positionMs: Long,
        isCompleted: Boolean
    ) {
        episodeLocalDataSource.updateProgress(episodeId, positionMs, isCompleted)
    }

    override suspend fun clearDownloadedState(episodeId: String) {
        episodeLocalDataSource.clearDownloadedState(episodeId)
    }

    override suspend fun delete(episode: Episode) {
        episodeLocalDataSource.delete(episode)
    }
}
