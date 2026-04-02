package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.source.RadioLocalDataSource
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.network.radio.model.RemoteRadioStation
import com.learpc.learpc.core.network.radio.source.RadioRemoteDataSource
import com.learpc.learpc.app.data.source.TaiwanRadioCatalogSource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultRadioRepositoryTest {
    @Test
    fun `refresh preserves custom station sort order`() = runTest {
        val initialStations = listOf(
            station(id = "custom-station", name = "Custom", sortOrder = 5),
            station(id = "existing-remote", name = "Existing Remote", sortOrder = 0)
        )
        val localFlow = MutableStateFlow(initialStations.sortedByStationOrder())

        val radioRemoteDataSource = mockk<RadioRemoteDataSource>()
        val radioLocalDataSource = mockk<RadioLocalDataSource>()
        val taiwanRadioCatalogSource = mockk<TaiwanRadioCatalogSource>()

        every { radioLocalDataSource.observeAll() } returns localFlow
        coEvery { taiwanRadioCatalogSource.loadStations(any(), any()) } returns Result.success(emptyList())
        coEvery { radioLocalDataSource.getById(any()) } answers {
            localFlow.value.firstOrNull { it.id == firstArg() }
        }
        coEvery { radioLocalDataSource.delete(any()) } answers {
            val station = firstArg<RadioStation>()
            localFlow.value = localFlow.value.filterNot { it.id == station.id }
        }
        coEvery { radioLocalDataSource.upsert(any()) } answers {
            val station = firstArg<RadioStation>()
            localFlow.value = localFlow.value
                .filterNot { it.id == station.id }
                .plus(station)
                .sortedByStationOrder()
        }
        coEvery { radioLocalDataSource.upsertAll(any()) } answers {
            val stations = firstArg<List<RadioStation>>()
            localFlow.value = stations.fold(localFlow.value) { current, station ->
                current
                    .filterNot { it.id == station.id }
                    .plus(station)
                    .sortedByStationOrder()
            }
        }

        coEvery { radioRemoteDataSource.fetchTopStations(any()) } returns Result.success(
            listOf(
                remoteStation(id = "existing-remote", name = "Existing Remote Updated"),
                remoteStation(id = "new-remote", name = "Brand New Remote")
            )
        )

        val repository = DefaultRadioRepository(
            radioRemoteDataSource = radioRemoteDataSource,
            radioLocalDataSource = radioLocalDataSource,
            taiwanRadioCatalogSource = taiwanRadioCatalogSource
        )

        val refreshedStations = repository.observeStations().drop(1).first()

        assertEquals(listOf("existing-remote", "custom-station", "new-remote"), refreshedStations.map { it.id })
        assertEquals(listOf(0, 5, 7), refreshedStations.mapNotNull { it.sortOrder })
    }

    @Test
    fun `search prefers local station when remote shares the same id`() = runTest {
        val localStation = station(
            id = "shared-station",
            name = "Shared Station",
            sortOrder = 2
        ).copy(
            streamUrl = "https://example.com/local.mp3",
            resolvedStreamUrl = "https://example.com/local-resolved.mp3"
        )
        val localFlow = MutableStateFlow(listOf(localStation))

        val radioRemoteDataSource = mockk<RadioRemoteDataSource>()
        val radioLocalDataSource = mockk<RadioLocalDataSource>()
        val taiwanRadioCatalogSource = mockk<TaiwanRadioCatalogSource>()

        every { radioLocalDataSource.observeAll() } returns localFlow
        coEvery { taiwanRadioCatalogSource.loadStations(any(), any()) } returns Result.success(emptyList())
        coEvery { radioRemoteDataSource.searchStations(any()) } returns Result.success(
            listOf(
                remoteStation(
                    id = "shared-station",
                    name = "Shared Station",
                    streamUrl = "https://example.com/remote.mp3",
                    resolvedStreamUrl = null
                ),
                remoteStation(
                    id = "other-station",
                    name = "Other Station",
                    streamUrl = "https://example.com/other.mp3",
                    resolvedStreamUrl = null
                )
            )
        )

        val repository = DefaultRadioRepository(
            radioRemoteDataSource = radioRemoteDataSource,
            radioLocalDataSource = radioLocalDataSource,
            taiwanRadioCatalogSource = taiwanRadioCatalogSource
        )

        val results = repository.searchStations("shared")

        assertEquals(listOf("shared-station", "other-station"), results.map { it.id })
        assertEquals("https://example.com/local.mp3", results.first().streamUrl)
        assertEquals("https://example.com/local-resolved.mp3", results.first().resolvedStreamUrl)
        assertTrue(results.first().isFavorite.not())
    }

    private fun station(
        id: String,
        name: String,
        sortOrder: Int
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            sortOrder = sortOrder,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }

    private fun remoteStation(
        id: String,
        name: String,
        streamUrl: String = "https://example.com/$id.mp3",
        resolvedStreamUrl: String? = null
    ): RemoteRadioStation {
        return RemoteRadioStation(
            stationUuid = id,
            name = name,
            streamUrl = streamUrl,
            resolvedStreamUrl = resolvedStreamUrl,
            homepageUrl = null,
            artworkUrl = null,
            country = null,
            countryCode = null,
            language = null,
            tags = null,
            codec = null,
            bitrateKbps = null
        )
    }

    private fun List<RadioStation>.sortedByStationOrder(): List<RadioStation> {
        return sortedWith(
            compareBy<RadioStation> { it.sortOrder ?: Int.MAX_VALUE }
                .thenBy { it.name.lowercase() }
        )
    }
}
