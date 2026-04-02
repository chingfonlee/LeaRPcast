package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RadioStationManagementUseCaseTest {
    private val repository = FakeRadioRepository(
        initialStations = listOf(
            station(id = "alpha", sortOrder = 0),
            station(id = "beta", sortOrder = 1),
            station(id = "gamma", sortOrder = 2)
        )
    )

    @Test
    fun `add radio station appends to end of ordered list`() = runTest {
        val useCase = AddRadioStationUseCase(repository)

        val created = useCase(
            RadioStationDraft(
                name = "Custom Radio",
                streamUrl = "https://example.com/live.mp3"
            )
        )

        assertNotNull(created.id)
        assertEquals("Custom Radio", created.name)
        assertEquals(3, created.sortOrder)
        assertEquals(4, repository.getStations().size)
    }

    @Test
    fun `update radio station preserves ordering metadata`() = runTest {
        val useCase = UpdateRadioStationUseCase(repository)

        val updated = useCase(
            stationId = "beta",
            draft = RadioStationDraft(
                name = "Beta FM",
                streamUrl = "https://example.com/beta.mp3"
            )
        )

        assertEquals("Beta FM", updated.name)
        assertEquals(1, updated.sortOrder)
        assertEquals("https://example.com/beta.mp3", updated.streamUrl)
    }

    @Test
    fun `delete radio station renumbers remaining stations`() = runTest {
        val useCase = DeleteRadioStationUseCase(repository)

        useCase("beta")

        val stations = repository.getStations()
        assertEquals(listOf("alpha", "gamma"), stations.map { it.id })
        assertEquals(listOf(0, 1), stations.mapNotNull { it.sortOrder })
    }

    @Test
    fun `reorder radio stations rewrites sort order`() = runTest {
        val useCase = ReorderRadioStationsUseCase(repository)

        useCase("alpha", 2)

        val stations = repository.getStations()
        assertEquals(listOf("beta", "gamma", "alpha"), stations.map { it.id })
        assertEquals(listOf(0, 1, 2), stations.mapNotNull { it.sortOrder })
    }

    private class FakeRadioRepository(
        initialStations: List<RadioStation>
    ) : RadioRepository {
        private val stations = initialStations.toMutableList()

        override fun observeStations(): Flow<List<RadioStation>> {
            return flowOf(sortedStations())
        }

        override suspend fun getStations(): List<RadioStation> {
            return sortedStations()
        }

        override suspend fun searchStations(query: String): List<RadioStation> {
            return emptyList()
        }

        override suspend fun getById(id: String): RadioStation? {
            return stations.firstOrNull { it.id == id }
        }

        override suspend fun upsert(station: RadioStation) {
            stations.removeAll { it.id == station.id }
            stations.add(station)
        }

        override suspend fun delete(station: RadioStation) {
            stations.removeAll { it.id == station.id }
        }

        private fun sortedStations(): List<RadioStation> {
            return stations.sortedWith(
                compareBy<RadioStation> { it.sortOrder ?: Int.MAX_VALUE }
                    .thenBy { it.name.lowercase() }
            )
        }
    }

    private fun station(
        id: String,
        sortOrder: Int,
        name: String = id
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
}
