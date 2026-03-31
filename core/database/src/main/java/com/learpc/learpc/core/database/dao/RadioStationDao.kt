package com.learpc.learpc.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.learpc.learpc.core.database.entities.RadioStationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioStationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: RadioStationEntity)

    @Upsert
    suspend fun upsert(station: RadioStationEntity)

    @Delete
    suspend fun delete(station: RadioStationEntity)

    @Query("SELECT * FROM radio_station WHERE station_id = :id LIMIT 1")
    suspend fun getById(id: String): RadioStationEntity?

    @Query("SELECT * FROM radio_station ORDER BY COALESCE(sort_order, 2147483647) ASC, is_favorite DESC, name COLLATE NOCASE")
    fun observeAll(): Flow<List<RadioStationEntity>>
}
