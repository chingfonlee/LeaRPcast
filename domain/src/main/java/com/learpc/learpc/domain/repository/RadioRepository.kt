package com.learpc.learpc.domain.repository

import com.learpc.learpc.core.model.radio.RadioStation
import kotlinx.coroutines.flow.Flow

interface RadioRepository {
    fun observeStations(): Flow<List<RadioStation>>
    suspend fun getStations(): List<RadioStation>
    suspend fun getById(id: String): RadioStation?
    suspend fun upsert(station: RadioStation)
    suspend fun delete(station: RadioStation)
}
