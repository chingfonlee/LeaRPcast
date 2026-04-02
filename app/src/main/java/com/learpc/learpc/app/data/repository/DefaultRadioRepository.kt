package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.mapper.RadioStationMapper.toDomain
import com.learpc.learpc.core.database.source.RadioLocalDataSource
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.network.radio.model.RemoteRadioStation
import com.learpc.learpc.core.network.radio.source.RadioRemoteDataSource
import com.learpc.learpc.app.data.source.TaiwanRadioCatalogSource
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
    private val radioLocalDataSource: RadioLocalDataSource,
    private val taiwanRadioCatalogSource: TaiwanRadioCatalogSource
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

    override suspend fun searchStations(query: String): List<RadioStation> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return emptyList()

        val remoteMatches = radioRemoteDataSource.searchStations(normalizedQuery)
            .getOrElse { emptyList() }
            .map { it.toDomain(existing = null, now = System.currentTimeMillis(), fallbackSortOrder = 0) }

        val localMatches = radioLocalDataSource.observeAll().first().filter { station ->
            matchesStationQuery(station, normalizedQuery)
        }

        return (localMatches + remoteMatches)
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<RadioStation> { it.name.contains(normalizedQuery, ignoreCase = true) }
                    .thenByDescending { it.country?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.language?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.genre?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenBy { it.name.lowercase() }
            )
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

        withContext(Dispatchers.IO) {
            taiwanRadioCatalogSource.loadStations(existingStations, now)
                .getOrElse { emptyList() }
                .let { radioLocalDataSource.upsertAll(it) }
        }

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
            displayName = displayName,
            displayFrequency = displayFrequency,
            frequency = frequency,
            band = band,
            sourceGroup = sourceGroup,
            network = network,
            region = region,
            category = category,
            mediaType = mediaType,
            uiPrimaryGroup = uiPrimaryGroup,
            uiSecondaryGroup = uiSecondaryGroup,
            country = country,
            countryCode = countryCode,
            language = language,
            genre = tags,
            codec = codec,
            bitrateKbps = bitrateKbps,
            searchKeywords = searchKeywords,
            aliases = aliases,
            mergedFromIds = mergedFromIds,
            isFavorite = existing?.isFavorite ?: false,
            sortOrder = existing?.sortOrder ?: fallbackSortOrder,
            lastSyncedAt = now,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )
    }

    private fun matchesStationQuery(station: RadioStation, query: String): Boolean {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return false

        val searchableText = buildList {
            add(station.name)
            station.displayName?.let(::add)
            station.displayFrequency?.let(::add)
            station.frequency?.let(::add)
            station.band?.let(::add)
            station.sourceGroup?.let(::add)
            station.network?.let(::add)
            station.region?.let(::add)
            station.category?.let(::add)
            station.mediaType?.let(::add)
            station.uiPrimaryGroup?.let(::add)
            station.uiSecondaryGroup?.let(::add)
            add(station.streamUrl)
            station.resolvedStreamUrl?.let(::add)
            station.country?.let(::add)
            station.countryCode?.let(::add)
            station.language?.let(::add)
            station.genre?.let(::add)
            station.codec?.let(::add)
            station.searchKeywords.forEach(::add)
            station.aliases.forEach(::add)
            station.mergedFromIds.forEach(::add)
        }.joinToString(" ")

        return searchableText.contains(normalizedQuery, ignoreCase = true)
    }
}
