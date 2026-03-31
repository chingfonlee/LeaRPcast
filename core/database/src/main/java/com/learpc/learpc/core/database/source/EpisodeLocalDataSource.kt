package com.learpc.learpc.core.database.source

import com.learpc.learpc.core.database.dao.EpisodeDao
import com.learpc.learpc.core.database.dao.PlaybackProgressDao
import com.learpc.learpc.core.database.entities.PlaybackProgressEntity
import com.learpc.learpc.core.database.mapper.EpisodeMapper.toDomain
import com.learpc.learpc.core.database.mapper.EpisodeMapper.toEntity
import com.learpc.learpc.core.model.podcast.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EpisodeLocalDataSource @Inject constructor(
    private val episodeDao: EpisodeDao,
    private val playbackProgressDao: PlaybackProgressDao
) {
    fun observeAll(): Flow<List<Episode>> {
        return episodeDao.observeAll().map { episodes ->
            episodes.map { it.toDomain() }
        }
    }

    fun observeByPodcastId(podcastId: String): Flow<List<Episode>> {
        return episodeDao.observeByPodcastId(podcastId).map { episodes ->
            episodes.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): Episode? {
        return episodeDao.getById(id)?.toDomain()
    }

    suspend fun upsert(episode: Episode) {
        episodeDao.upsert(episode.toEntity())
    }

    suspend fun updateProgress(
        episodeId: String,
        positionMs: Long,
        isCompleted: Boolean
    ) {
        val episode = episodeDao.getById(episodeId) ?: return
        val now = System.currentTimeMillis()
        val existing = playbackProgressDao.getByEpisodeId(episodeId)
        val completed = isCompleted || existing?.completionState == COMPLETION_STATE_COMPLETED
        val playCount = when {
            isCompleted && existing?.completionState != COMPLETION_STATE_COMPLETED ->
                (existing?.playCount ?: 0) + 1
            else -> existing?.playCount ?: 0
        }

        playbackProgressDao.upsert(
            PlaybackProgressEntity(
                episodeId = episodeId,
                positionMs = positionMs,
                durationMs = episode.durationMs,
                completionState = if (completed) COMPLETION_STATE_COMPLETED else COMPLETION_STATE_IN_PROGRESS,
                playCount = playCount,
                lastPlayedAt = now,
                completedAt = if (completed) existing?.completedAt ?: now else existing?.completedAt,
                updatedAt = now
            )
        )
    }

    suspend fun clearDownloadedState(episodeId: String) {
        episodeDao.updateDownloadedState(
            episodeId = episodeId,
            isDownloaded = false,
            updatedAt = System.currentTimeMillis()
        )
    }

    suspend fun delete(episode: Episode) {
        episodeDao.delete(episode.toEntity())
    }

    private companion object {
        const val COMPLETION_STATE_COMPLETED = "COMPLETED"
        const val COMPLETION_STATE_IN_PROGRESS = "IN_PROGRESS"
    }
}
