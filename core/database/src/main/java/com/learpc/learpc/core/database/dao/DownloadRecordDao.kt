package com.learpc.learpc.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.learpc.learpc.core.database.entities.DownloadRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: DownloadRecordEntity)

    @Upsert
    suspend fun upsert(record: DownloadRecordEntity)

    @Delete
    suspend fun delete(record: DownloadRecordEntity)

    @Query("SELECT * FROM download_record ORDER BY downloaded_at DESC")
    fun observeAll(): Flow<List<DownloadRecordEntity>>

    @Query("SELECT * FROM download_record WHERE episode_id = :episodeId LIMIT 1")
    suspend fun getByEpisodeId(episodeId: String): DownloadRecordEntity?
}
