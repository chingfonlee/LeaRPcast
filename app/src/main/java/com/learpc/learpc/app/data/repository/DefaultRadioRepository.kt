package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.mapper.RadioStationMapper.toDomain
import com.learpc.learpc.core.database.source.RadioLocalDataSource
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.network.radio.model.RemoteRadioStation
import com.learpc.learpc.core.network.radio.source.RadioRemoteDataSource
import com.learpc.learpc.domain.repository.RadioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val DEFAULT_TOP_STATIONS_LIMIT = 50

class DefaultRadioRepository @Inject constructor(
    private val radioRemoteDataSource: RadioRemoteDataSource,
    private val radioLocalDataSource: RadioLocalDataSource
) : RadioRepository {
    override fun observeStations(): Flow<List<RadioStation>> = callbackFlow {
        val localCollector = launch {
            radioLocalDataSource.observeAll().collect { stations ->
                trySend(stations)
            }
        }

        launch {
            refreshStations()
        }

        awaitClose {
            localCollector.cancel()
        }
    }

    override suspend fun getStations(): List<RadioStation> {
        return radioLocalDataSource.observeAll().first()
    }

    override suspend fun getById(id: String): RadioStation? {
        return radioLocalDataSource.getById(id)
    }

    override suspend fun upsert(station: RadioStation) {
        radioLocalDataSource.upsert(station)
    }

    override suspend fun delete(station: RadioStation) {
        radioLocalDataSource.delete(station)
    }

    private suspend fun refreshStations() {
        val now = System.currentTimeMillis()
        val existingStations = radioLocalDataSource.observeAll().first().associateBy { it.id }
        val nextSortOrder = existingStations.values
            .mapNotNull { it.sortOrder }
            .maxOrNull()
            ?.plus(1)
            ?: 0

        val remoteStations = radioRemoteDataSource.fetchTopStations(DEFAULT_TOP_STATIONS_LIMIT)
            .getOrElse { return }

        withContext(Dispatchers.IO) {
            remoteStations.mapIndexed { index, remoteStation ->
                remoteStation.toDomain(
                    existing = existingStations[remoteStation.stationUuid],
                    now = now,
                    fallbackSortOrder = nextSortOrder + index
                )
            }.let { radioLocalDataSource.upsertAll(it) }
        }
    }

    private fun RemoteRadioStation.toDomain(
        existing: RadioStation?,
        now: Long,
        fallbackSortOrder: Int
    ): RadioStation {
        return RadioStation(
            id = stationUuid,
            name = name,
            streamUrl = resolvedStreamUrl ?: streamUrl,
            resolvedStreamUrl = resolvedStreamUrl,
            homepageUrl = homepageUrl,
            artworkUrl = artworkUrl,
            country = country,
            countryCode = countryCode,
            language = language,
            genre = tags,
            codec = codec,
            bitrateKbps = bitrateKbps,
            isFavorite = existing?.isFavorite ?: false,
            sortOrder = existing?.sortOrder ?: fallbackSortOrder,
            lastSyncedAt = now,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )
    }
}
