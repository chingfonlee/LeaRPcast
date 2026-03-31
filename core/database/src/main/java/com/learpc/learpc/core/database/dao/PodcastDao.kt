package com.learpc.learpc.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.learpc.learpc.core.database.dto.PodcastWithEpisodes
import com.learpc.learpc.core.database.dto.PodcastWithSubscriptionSettings
import com.learpc.learpc.core.database.entities.PodcastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(podcast: PodcastEntity)

    @Upsert
    suspend fun upsert(podcast: PodcastEntity)

    @Delete
    suspend fun delete(podcast: PodcastEntity)

    @Query("SELECT * FROM podcast ORDER BY title COLLATE NOCASE")
    fun observeAll(): Flow<List<PodcastEntity>>

    @Query("SELECT * FROM podcast WHERE podcast_id = :id LIMIT 1")
    suspend fun getById(id: String): PodcastEntity?

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcast_id = :podcastId LIMIT 1")
    fun observeWithEpisodes(podcastId: String): Flow<PodcastWithEpisodes?>

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcast_id = :podcastId LIMIT 1")
    fun observeWithSubscriptionSettings(podcastId: String): Flow<PodcastWithSubscriptionSettings?>

    @Transaction
    @Query("SELECT * FROM podcast WHERE podcast_id = :podcastId LIMIT 1")
    suspend fun getSubscriptionSettings(podcastId: String): PodcastWithSubscriptionSettings?
}
