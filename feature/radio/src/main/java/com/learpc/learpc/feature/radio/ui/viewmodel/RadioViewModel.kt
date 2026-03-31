package com.learpc.learpc.feature.radio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import com.learpc.learpc.domain.usecase.PlayRadioUseCase
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
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
    private val playRadioUseCase: PlayRadioUseCase
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

                    uiStateMutable.value = RadioUiState(
                        isLoading = stations.isEmpty() && !hasReceivedStations,
                        stations = stations,
                        errorMessage = null
                    )
                }
            } catch (throwable: Throwable) {
                loadingTimeoutJob?.cancel()
                uiStateMutable.value = RadioUiState(
                    isLoading = false,
                    stations = emptyList(),
                    errorMessage = throwable.message ?: "Could not load radio stations."
                )
            }
        }
    }

    fun playStation(station: RadioStation) {
        playRadioUseCase(station)
    }
}
