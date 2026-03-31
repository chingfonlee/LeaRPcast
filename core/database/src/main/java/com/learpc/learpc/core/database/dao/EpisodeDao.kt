package com.learpc.learpc.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.learpc.learpc.core.database.entities.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: EpisodeEntity)

    @Upsert
    suspend fun upsert(episode: EpisodeEntity)

    @Query("DELETE FROM episode WHERE podcast_id = :podcastId")
    suspend fun deleteByPodcastId(podcastId: String)

    @Delete
    suspend fun delete(episode: EpisodeEntity)

    @Query("SELECT * FROM episode ORDER BY published_at DESC")
    fun observeAll(): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episode WHERE episode_id = :id LIMIT 1")
    suspend fun getById(id: String): EpisodeEntity?

    @Query("UPDATE episode SET is_downloaded = :isDownloaded, updated_at = :updatedAt WHERE episode_id = :episodeId")
    suspend fun updateDownloadedState(
        episodeId: String,
        isDownloaded: Boolean,
        updatedAt: Long
    )

    @Query("SELECT * FROM episode WHERE podcast_id = :podcastId ORDER BY published_at DESC")
    fun observeByPodcastId(podcastId: String): Flow<List<EpisodeEntity>>
}
