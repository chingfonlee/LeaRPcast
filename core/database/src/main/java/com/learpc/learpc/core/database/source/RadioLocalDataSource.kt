package com.learpc.learpc.core.database.source

import com.learpc.learpc.core.database.dao.RadioStationDao
import com.learpc.learpc.core.database.mapper.RadioStationMapper.toDomain
import com.learpc.learpc.core.database.mapper.RadioStationMapper.toEntity
import com.learpc.learpc.core.model.radio.RadioStation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RadioLocalDataSource @Inject constructor(
    private val radioStationDao: RadioStationDao
) {
    fun observeAll(): Flow<List<RadioStation>> {
        return radioStationDao.observeAll().map { stations ->
            stations.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): RadioStation? {
        return radioStationDao.getById(id)?.toDomain()
    }

    suspend fun upsert(station: RadioStation) {
        radioStationDao.upsert(station.toEntity())
    }

    suspend fun upsertAll(stations: List<RadioStation>) {
        stations.forEach { station ->
            radioStationDao.upsert(station.toEntity())
        }
    }

    suspend fun delete(station: RadioStation) {
        radioStationDao.delete(station.toEntity())
    }
}
