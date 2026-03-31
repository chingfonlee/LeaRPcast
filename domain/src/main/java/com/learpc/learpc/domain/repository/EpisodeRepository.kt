package com.learpc.learpc.domain.repository

import com.learpc.learpc.core.model.podcast.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun observeByPodcastId(podcastId: String): Flow<List<Episode>>
    fun observeAll(): Flow<List<Episode>>
    suspend fun getById(id: String): Episode?
    suspend fun upsert(episode: Episode)
    suspend fun updateProgress(episodeId: String, positionMs: Long, isCompleted: Boolean = false)
    suspend fun clearDownloadedState(episodeId: String)
    suspend fun delete(episode: Episode)
}
