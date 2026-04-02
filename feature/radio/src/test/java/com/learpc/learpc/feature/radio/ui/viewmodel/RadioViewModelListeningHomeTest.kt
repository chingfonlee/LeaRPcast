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
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RadioViewModelListeningHomeTest {
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
    fun `empty saved stations surface listening empty state`() = runTest {
        val station = station("starter-station")
        every { radioRepository.observeStations() } returns flowOf(listOf(station))
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

        assertEquals(RadioHomeStatus.Empty, viewModel.uiState.value.homeStatus)
        assertEquals(listOf(station.id), viewModel.uiState.value.stations.map { it.id })
        assertEquals(emptyList(), viewModel.uiState.value.savedStations)
    }

    @Test
    fun `playing station updates listening home banner`() = runTest {
        val station = station("live-station", isFavorite = true)
        every { radioRepository.observeStations() } returns flowOf(listOf(station))
        coEvery { radioRepository.getById(any()) } returns station

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

        playbackStateFlow.value = PlaybackStateModel.Playing
        currentItemFlow.value = PlayableItem(
            id = station.id,
            title = station.name,
            mediaUri = station.streamUrl
        )
        advanceUntilIdle()

        assertEquals(RadioHomeStatus.Playing, viewModel.uiState.value.homeStatus)
        assertEquals(station.id, viewModel.uiState.value.currentItemId)
    }

    @Test
    fun `toggling favorite promotes station into saved list`() = runTest {
        val station = station("toggle-station")
        every { radioRepository.observeStations() } returns flowOf(listOf(station))
        coEvery { radioRepository.getById(station.id) } returns station
        coEvery { setRadioStationFavoriteUseCase(station.id, true) } returns station.copy(isFavorite = true)

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
        viewModel.toggleFavorite(station)
        advanceUntilIdle()

        assertEquals(listOf(station.id), viewModel.uiState.value.savedStations.map { it.id })
    }

    private fun station(
        id: String,
        name: String = "Station $id",
        isFavorite: Boolean = false
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            country = "Taiwan",
            language = "zh",
            isFavorite = isFavorite,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
