package com.learpc.learpc.feature.radio.ui.viewmodel

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import com.learpc.learpc.domain.usecase.AddRadioStationUseCase
import com.learpc.learpc.domain.usecase.DeleteRadioStationUseCase
import com.learpc.learpc.domain.usecase.PlayRadioUseCase
import com.learpc.learpc.domain.usecase.ReorderRadioStationsUseCase
import com.learpc.learpc.domain.usecase.SaveRadioStationUseCase
import com.learpc.learpc.domain.usecase.SetRadioStationFavoriteUseCase
import com.learpc.learpc.domain.usecase.UpdateRadioStationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RadioViewModelDiscoveryActionTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val radioRepository = mockk<RadioRepository>()
    private val playbackStateFlow = MutableStateFlow<PlaybackStateModel>(PlaybackStateModel.Idle)
    private val currentItemFlow = MutableStateFlow<PlayableItem?>(null)
    private val playbackController = mockk<PlaybackController> {
        every { playbackState } returns playbackStateFlow
        every { currentItem } returns currentItemFlow
    }
    private val playRadioUseCase = mockk<PlayRadioUseCase>(relaxed = true)
    private val addRadioStationUseCase = mockk<AddRadioStationUseCase>(relaxed = true)
    private val updateRadioStationUseCase = mockk<UpdateRadioStationUseCase>(relaxed = true)
    private val deleteRadioStationUseCase = mockk<DeleteRadioStationUseCase>(relaxed = true)
    private val reorderRadioStationsUseCase = mockk<ReorderRadioStationsUseCase>(relaxed = true)
    private val setRadioStationFavoriteUseCase = mockk<SetRadioStationFavoriteUseCase>(relaxed = true)
    private val saveRadioStationUseCase = mockk<SaveRadioStationUseCase>(relaxed = true)

    @Test
    fun `recommended station can preview and save without management page`() = runTest {
        val station = station("discovery-station")
        every { radioRepository.observeStations() } returns flowOf(listOf(station))
        coEvery { radioRepository.getById(station.id) } returns null
        coEvery { saveRadioStationUseCase(station) } returns station.copy(isFavorite = true)

        val viewModel = RadioViewModel(
            radioRepository = radioRepository,
            playbackController = playbackController,
            playRadioUseCase = playRadioUseCase,
            addRadioStationUseCase = addRadioStationUseCase,
            updateRadioStationUseCase = updateRadioStationUseCase,
            deleteRadioStationUseCase = deleteRadioStationUseCase,
            reorderRadioStationsUseCase = reorderRadioStationsUseCase,
            setRadioStationFavoriteUseCase = setRadioStationFavoriteUseCase,
            saveRadioStationUseCase = saveRadioStationUseCase
        )

        viewModel.playStation(station)
        viewModel.saveRecommendedStation(station)
        advanceUntilIdle()

        coVerify(exactly = 1) { playRadioUseCase(station) }
        coVerify(exactly = 1) { saveRadioStationUseCase(station) }
        assertEquals(listOf(station.id), viewModel.uiState.value.savedStations.map { it.id })
        assertEquals(0, viewModel.uiState.value.recommendedStations.size)
    }

    @Test
    fun `selecting discovery type reprioritizes recommendations immediately`() = runTest {
        val jazzStation = station("jazz-station", name = "Jazz Station", genre = "Jazz", country = "Taiwan")
        val newsStation = station("news-station", name = "News Station", genre = "News", country = "Japan")
        every { radioRepository.observeStations() } returns flowOf(listOf(newsStation, jazzStation))
        coEvery { radioRepository.getById(any()) } returns null

        val viewModel = RadioViewModel(
            radioRepository = radioRepository,
            playbackController = playbackController,
            playRadioUseCase = playRadioUseCase,
            addRadioStationUseCase = addRadioStationUseCase,
            updateRadioStationUseCase = updateRadioStationUseCase,
            deleteRadioStationUseCase = deleteRadioStationUseCase,
            reorderRadioStationsUseCase = reorderRadioStationsUseCase,
            setRadioStationFavoriteUseCase = setRadioStationFavoriteUseCase,
            saveRadioStationUseCase = saveRadioStationUseCase
        )

        advanceUntilIdle()

        viewModel.selectDiscoveryType("Jazz")
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assertEquals("Jazz", uiState.selectedDiscoveryType)
        assertEquals(listOf("jazz-station", "news-station"), uiState.recommendedStations.map { it.id })
    }

    private fun station(id: String): RadioStation {
        return station(id, "Discovery Station")
    }

    private fun station(
        id: String,
        name: String,
        genre: String? = null,
        country: String? = null,
        sortOrder: Int = 0
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            country = country,
            language = "zh",
            genre = genre,
            sortOrder = sortOrder,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
