package com.learpc.learpc.feature.radio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import com.learpc.learpc.domain.usecase.AddRadioStationUseCase
import com.learpc.learpc.domain.usecase.DeleteRadioStationUseCase
import com.learpc.learpc.domain.usecase.ReorderRadioStationsUseCase
import com.learpc.learpc.domain.usecase.PlayRadioUseCase
import com.learpc.learpc.domain.usecase.UpdateRadioStationUseCase
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
import com.learpc.learpc.feature.radio.ui.model.RadioStationEditorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val radioRepository: RadioRepository,
    private val playRadioUseCase: PlayRadioUseCase,
    private val addRadioStationUseCase: AddRadioStationUseCase,
    private val updateRadioStationUseCase: UpdateRadioStationUseCase,
    private val deleteRadioStationUseCase: DeleteRadioStationUseCase,
    private val reorderRadioStationsUseCase: ReorderRadioStationsUseCase
) : ViewModel() {
    private val uiStateMutable = MutableStateFlow(RadioUiState())
    val uiState: StateFlow<RadioUiState> = uiStateMutable.asStateFlow()

    private var loadingTimeoutJob: Job? = null
    private var hasReceivedStations = false

    init {
        viewModelScope.launch {
            try {
                radioRepository.observeStations().collect { stations ->
                    if (stations.isNotEmpty()) {
                        hasReceivedStations = true
                        loadingTimeoutJob?.cancel()
                        loadingTimeoutJob = null
                    } else if (!hasReceivedStations && loadingTimeoutJob == null) {
                        loadingTimeoutJob = launch {
                            delay(2_000)
                            if (!hasReceivedStations && uiStateMutable.value.stations.isEmpty()) {
                                uiStateMutable.value = uiStateMutable.value.copy(isLoading = false)
                            }
                            loadingTimeoutJob = null
                        }
                    }

                    val currentState = uiStateMutable.value
                    uiStateMutable.value = currentState.copy(
                        isLoading = stations.isEmpty() && !hasReceivedStations,
                        stations = stations,
                        errorMessage = null,
                        stationEditor = currentState.stationEditor
                    )
                }
            } catch (throwable: Throwable) {
                loadingTimeoutJob?.cancel()
                val currentState = uiStateMutable.value
                uiStateMutable.value = currentState.copy(
                    isLoading = false,
                    stations = emptyList(),
                    errorMessage = throwable.message ?: "Could not load radio stations.",
                    stationEditor = currentState.stationEditor
                )
            }
        }
    }

    fun playStation(station: RadioStation) {
        playRadioUseCase(station)
    }

    fun showAddStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = RadioStationEditorState()
        )
    }

    fun showEditStationEditor(station: RadioStation) {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = station.toEditorState()
        )
    }

    fun updateStationEditor(editorState: RadioStationEditorState) {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = editorState.copy(validationError = null)
        )
    }

    fun dismissStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = null
        )
    }

    fun saveStation() {
        val editorState = uiStateMutable.value.stationEditor ?: return
        val name = editorState.name.trim()
        val streamUrl = editorState.streamUrl.trim()
        if (name.isBlank() || streamUrl.isBlank()) {
            uiStateMutable.value = uiStateMutable.value.copy(
                stationEditor = editorState.copy(
                    validationError = "Station name and stream URL are required."
                )
            )
            return
        }

        viewModelScope.launch {
            runCatching {
                val draft = editorState.toDraft(name, streamUrl)
                if (editorState.stationId == null) {
                    addRadioStationUseCase(draft)
                } else {
                    updateRadioStationUseCase(editorState.stationId, draft)
                }
            }.onSuccess {
                dismissStationEditor()
            }.onFailure { throwable ->
                uiStateMutable.value = uiStateMutable.value.copy(
                    stationEditor = editorState.copy(
                        validationError = throwable.message ?: "Could not save station."
                    )
                )
            }
        }
    }

    fun deleteStation(station: RadioStation) {
        viewModelScope.launch {
            runCatching {
                deleteRadioStationUseCase(station.id)
            }
            if (uiStateMutable.value.stationEditor?.stationId == station.id) {
                dismissStationEditor()
            }
        }
    }

    fun moveStationUp(station: RadioStation) {
        moveStation(station, -1)
    }

    fun moveStationDown(station: RadioStation) {
        moveStation(station, 1)
    }

    private fun moveStation(station: RadioStation, offset: Int) {
        val stations = uiStateMutable.value.stations
        val currentIndex = stations.indexOfFirst { it.id == station.id }
        if (currentIndex == -1) return

        val targetIndex = currentIndex + offset
        if (targetIndex !in stations.indices) return

        viewModelScope.launch {
            reorderRadioStationsUseCase(station.id, targetIndex)
        }
    }

    private fun RadioStation.toEditorState(): RadioStationEditorState {
        return RadioStationEditorState(
            stationId = id,
            name = name,
            streamUrl = streamUrl,
            resolvedStreamUrl = resolvedStreamUrl.orEmpty(),
            homepageUrl = homepageUrl.orEmpty(),
            artworkUrl = artworkUrl.orEmpty(),
            country = country.orEmpty(),
            language = language.orEmpty(),
            genre = genre.orEmpty()
        )
    }

    private fun RadioStationEditorState.toDraft(
        name: String,
        streamUrl: String
    ): RadioStationDraft {
        return RadioStationDraft(
            name = name,
            streamUrl = streamUrl,
            resolvedStreamUrl = resolvedStreamUrl.trim().ifBlank { null },
            homepageUrl = homepageUrl.trim().ifBlank { null },
            artworkUrl = artworkUrl.trim().ifBlank { null },
            country = country.trim().ifBlank { null },
            language = language.trim().ifBlank { null },
            genre = genre.trim().ifBlank { null }
        )
    }
}
