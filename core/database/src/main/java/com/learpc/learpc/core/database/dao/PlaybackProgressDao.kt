package com.learpc.learpc.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.learpc.learpc.core.database.entities.PlaybackProgressEntity

@Dao
interface PlaybackProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: PlaybackProgressEntity)

    @Upsert
    suspend fun upsert(progress: PlaybackProgressEntity)

    @Query("SELECT * FROM playback_progress WHERE episode_id = :episodeId LIMIT 1")
    suspend fun getByEpisodeId(episodeId: String): PlaybackProgressEntity?
}
