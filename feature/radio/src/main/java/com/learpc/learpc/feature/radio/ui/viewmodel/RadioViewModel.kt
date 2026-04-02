package com.learpc.learpc.feature.radio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.PlaybackError
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import com.learpc.learpc.domain.usecase.AddRadioStationUseCase
import com.learpc.learpc.domain.usecase.DeleteRadioStationUseCase
import com.learpc.learpc.domain.usecase.PlayRadioUseCase
import com.learpc.learpc.domain.usecase.ReorderRadioStationsUseCase
import com.learpc.learpc.domain.usecase.SaveRadioStationUseCase
import com.learpc.learpc.domain.usecase.SetRadioStationFavoriteUseCase
import com.learpc.learpc.domain.usecase.UpdateRadioStationUseCase
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import com.learpc.learpc.feature.radio.ui.model.RadioBrowseSortMode
import com.learpc.learpc.feature.radio.ui.model.RadioQuickAddState
import com.learpc.learpc.feature.radio.ui.model.RadioShellTab
import com.learpc.learpc.feature.radio.ui.model.RadioStationsTaxonomyMode
import com.learpc.learpc.feature.radio.ui.model.RadioStationEditorState
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val radioRepository: RadioRepository,
    private val playbackController: PlaybackController,
    private val playRadioUseCase: PlayRadioUseCase,
    private val addRadioStationUseCase: AddRadioStationUseCase,
    private val updateRadioStationUseCase: UpdateRadioStationUseCase,
    private val deleteRadioStationUseCase: DeleteRadioStationUseCase,
    private val reorderRadioStationsUseCase: ReorderRadioStationsUseCase,
    private val setRadioStationFavoriteUseCase: SetRadioStationFavoriteUseCase,
    private val saveRadioStationUseCase: SaveRadioStationUseCase
) : ViewModel() {
    private val uiStateMutable = MutableStateFlow(RadioUiState())
    val uiState: StateFlow<RadioUiState> = uiStateMutable.asStateFlow()

    private var stationObservationJob: Job? = null
    private var playbackObservationJob: Job? = null
    private var loadingTimeoutJob: Job? = null
    private var searchJob: Job? = null
    private var hasReceivedStations = false
    private var latestStations: List<RadioStation> = emptyList()
    private val recentStationIds = mutableListOf<String>()

    init {
        observeStations()
        observePlayback()
    }

    fun selectShellTab(tab: RadioShellTab) {
        uiStateMutable.value = uiStateMutable.value.copy(
            selectedShellTab = tab
        )
    }

    fun selectBrowseSortMode(mode: RadioBrowseSortMode) {
        uiStateMutable.value = uiStateMutable.value.copy(
            browseSortMode = mode
        )
    }

    fun selectStationsTaxonomyMode(mode: RadioStationsTaxonomyMode) {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationsTaxonomyMode = mode
        )
    }

    fun updateSearchQuery(query: String) {
        val normalizedQuery = query.trim()
        val currentStations = uiStateMutable.value.stations

        uiStateMutable.value = uiStateMutable.value.copy(
            searchQuery = normalizedQuery,
            searchResults = if (normalizedQuery.isBlank()) {
                emptyList()
            } else {
                searchStations(currentStations, normalizedQuery)
            }
        )
    }

    fun togglePlayback() {
        val currentState = uiStateMutable.value
        val currentStation = currentStation(currentState) ?: return
        when (currentState.playbackState) {
            PlaybackStateModel.Playing -> playbackController.pause()
            else -> playStation(currentStation)
        }
    }

    fun retryPlayback() {
        currentStation(uiStateMutable.value)?.let(::playStation)
    }

    fun retryLoadStations() {
        hasReceivedStations = false
        loadingTimeoutJob?.cancel()
        loadingTimeoutJob = null
        uiStateMutable.value = uiStateMutable.value.copy(
            homeStatus = RadioHomeStatus.Loading,
            isLoading = true,
            networkErrorMessage = null,
            errorMessage = null
        )
        observeStations()
    }

    fun playStation(station: RadioStation) {
        recordRecentStation(station)
        uiStateMutable.value = uiStateMutable.value.copy(
            currentItemId = station.id,
            playbackState = PlaybackStateModel.Buffering,
            homeStatus = RadioHomeStatus.Playing,
            playbackErrorMessage = null,
            networkErrorMessage = null,
            errorMessage = null
        )

        viewModelScope.launch {
            runCatching {
                playRadioUseCase(station)
            }.onFailure { throwable ->
                setPlaybackFailure(
                    throwable.message ?: "Could not start playback."
                )
            }
        }
    }

    fun toggleFavorite(station: RadioStation) {
        viewModelScope.launch {
            runCatching {
                setRadioStationFavoriteUseCase(station.id, !station.isFavorite)
            }.onSuccess { updatedStation ->
                applySavedStation(updatedStation)
                if (updatedStation.isFavorite) {
                    announceAddSuccess(updatedStation.name)
                } else {
                    recomputeListeningState()
                }
            }.onFailure { throwable ->
                uiStateMutable.value = uiStateMutable.value.copy(
                    homeStatus = RadioHomeStatus.NetworkFailure,
                    networkErrorMessage = throwable.message ?: "Could not update station.",
                    errorMessage = throwable.message ?: "Could not update station."
                )
            }
        }
    }

    fun saveRecommendedStation(station: RadioStation) {
        viewModelScope.launch {
            runCatching {
                val savedStation = saveRadioStation(station)
                applySavedStation(savedStation)
                announceAddSuccess(savedStation.name)
            }.onFailure { throwable ->
                uiStateMutable.value = uiStateMutable.value.copy(
                    homeStatus = RadioHomeStatus.NetworkFailure,
                    networkErrorMessage = throwable.message ?: "Could not save station.",
                    errorMessage = throwable.message ?: "Could not save station."
                )
            }
        }
    }

    fun toggleDiscoverySearch() {
        val currentState = uiStateMutable.value
        val nextExpanded = !currentState.isDiscoverySearchExpanded
        uiStateMutable.value = currentState.copy(
            isDiscoverySearchExpanded = nextExpanded,
            discoveryQuery = if (nextExpanded) currentState.discoveryQuery else "",
            isSearching = false,
            searchResults = if (nextExpanded) currentState.searchResults else emptyList()
        )
    }

    fun selectTaxonomyType(type: String?) {
        uiStateMutable.value = uiStateMutable.value.copy(
            selectedTaxonomyType = type?.takeIf { it.isNotBlank() }
        )
        refreshDiscoveryRecommendations()
    }

    fun selectTaxonomyRegion(region: String?) {
        uiStateMutable.value = uiStateMutable.value.copy(
            selectedTaxonomyRegion = region?.takeIf { it.isNotBlank() }
        )
        refreshDiscoveryRecommendations()
    }

    fun showAddStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = RadioStationEditorState()
        )
    }

    fun showQuickAddStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            quickAddEditor = RadioQuickAddState()
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

    fun updateQuickAddEditor(editorState: RadioQuickAddState) {
        uiStateMutable.value = uiStateMutable.value.copy(
            quickAddEditor = editorState.copy(validationError = null)
        )
    }

    fun updateDiscoveryQuery(query: String) {
        val normalizedQuery = query.trim()
        val currentStations = uiStateMutable.value.stations
        searchJob?.cancel()

        if (normalizedQuery.isBlank()) {
            uiStateMutable.value = uiStateMutable.value.copy(
                discoveryQuery = "",
                isSearching = false,
                searchResults = emptyList(),
                isDiscoverySearchExpanded = true
            )
            return
        }

        uiStateMutable.value = uiStateMutable.value.copy(
            discoveryQuery = normalizedQuery,
            isSearching = true,
            isDiscoverySearchExpanded = true
        )

        searchJob = viewModelScope.launch {
            val remoteMatches = runCatching {
                radioRepository.searchStations(normalizedQuery)
            }.getOrElse { emptyList() }

            val localMatches = currentStations.filter { station ->
                station.name.contains(normalizedQuery, ignoreCase = true) ||
                    (station.country?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.language?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.genre?.contains(normalizedQuery, ignoreCase = true) == true)
            }

            val mergedResults = (remoteMatches + localMatches)
                .distinctBy { it.id }
                .sortedWith(
                    compareByDescending<RadioStation> { it.name.contains(normalizedQuery, ignoreCase = true) }
                        .thenByDescending { it.country?.contains(normalizedQuery, ignoreCase = true) == true }
                        .thenByDescending { it.language?.contains(normalizedQuery, ignoreCase = true) == true }
                        .thenByDescending { it.genre?.contains(normalizedQuery, ignoreCase = true) == true }
                        .thenBy { it.name.lowercase(Locale.ROOT) }
                )

            uiStateMutable.value = uiStateMutable.value.copy(
                searchResults = mergedResults,
                isSearching = false
            )
        }
    }

    fun dismissStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            stationEditor = null
        )
    }

    fun dismissQuickAddStationEditor() {
        uiStateMutable.value = uiStateMutable.value.copy(
            quickAddEditor = null
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
                recomputeListeningState()
            }.onFailure { throwable ->
                uiStateMutable.value = uiStateMutable.value.copy(
                    stationEditor = editorState.copy(
                        validationError = throwable.message ?: "Could not save station."
                    )
                )
            }
        }
    }

    fun saveQuickAddStation() {
        val editorState = uiStateMutable.value.quickAddEditor ?: return
        val name = editorState.name.trim()
        val streamUrl = editorState.streamUrl.trim()
        if (name.isBlank() || streamUrl.isBlank()) {
            uiStateMutable.value = uiStateMutable.value.copy(
                quickAddEditor = editorState.copy(
                    validationError = "Station name and stream URL are required."
                )
            )
            return
        }

        viewModelScope.launch {
            runCatching {
                val addedStation = addRadioStationUseCase(
                    RadioStationDraft(
                        name = name,
                        streamUrl = streamUrl
                    )
                )
                saveRadioStationUseCase(addedStation)
            }.onSuccess { savedStation ->
                dismissQuickAddStationEditor()
                applySavedStation(savedStation)
                announceAddSuccess(savedStation.name)
            }.onFailure { throwable ->
                uiStateMutable.value = uiStateMutable.value.copy(
                    quickAddEditor = editorState.copy(
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
            recomputeListeningState()
        }
    }

    fun moveStationUp(station: RadioStation) {
        moveStation(station, -1)
    }

    fun moveStationDown(station: RadioStation) {
        moveStation(station, 1)
    }

    private fun observeStations() {
        stationObservationJob?.cancel()
        stationObservationJob = viewModelScope.launch {
            try {
                radioRepository.observeStations().collect { stations ->
                    handleStationsUpdate(stations)
                }
            } catch (throwable: Throwable) {
                loadingTimeoutJob?.cancel()
                uiStateMutable.value = uiStateMutable.value.copy(
                    isLoading = false,
                    homeStatus = RadioHomeStatus.NetworkFailure,
                    stations = latestStations.ifNotEmptyOr(uiStateMutable.value.stations),
                    savedStations = latestStations.ifNotEmptyOr(uiStateMutable.value.stations)
                        .filter { it.isFavorite },
                    networkErrorMessage = throwable.message ?: "Could not load radio stations.",
                    errorMessage = throwable.message ?: "Could not load radio stations."
                )
            }
        }
    }

    private fun observePlayback() {
        playbackObservationJob?.cancel()
        playbackObservationJob = viewModelScope.launch {
            combine(
                playbackController.playbackState,
                playbackController.currentItem
            ) { playbackState, currentItem ->
                playbackState to currentItem
            }.collect { (playbackState, currentItem) ->
                val currentState = uiStateMutable.value
                val nextCurrentStation = currentItem?.id?.let { stationId ->
                    findStationById(stationId, currentState.stations)
                        ?: findStationById(stationId, currentState.savedStations)
                        ?: findStationById(stationId, currentState.recentStations)
                }
                uiStateMutable.value = currentState.copy(
                    playbackState = playbackState,
                    currentItemId = currentItem?.id,
                    playbackErrorMessage = when (playbackState) {
                        is PlaybackStateModel.Error -> playbackState.error.toDisplayMessage()
                        else -> currentState.playbackErrorMessage
                    }
                )
                nextCurrentStation?.let { recordRecentStation(it) }
                recomputeListeningState()
            }
        }
    }

    private fun handleStationsUpdate(stations: List<RadioStation>) {
        latestStations = stations
        if (stations.isNotEmpty()) {
            hasReceivedStations = true
            loadingTimeoutJob?.cancel()
            loadingTimeoutJob = null
        } else if (!hasReceivedStations && loadingTimeoutJob == null) {
            loadingTimeoutJob = viewModelScope.launch {
                delay(2_000)
                if (!hasReceivedStations && uiStateMutable.value.stations.isEmpty()) {
                    uiStateMutable.value = uiStateMutable.value.copy(
                        isLoading = false,
                        homeStatus = RadioHomeStatus.NetworkFailure,
                        networkErrorMessage = "Could not load radio stations.",
                        errorMessage = "Could not load radio stations."
                    )
                }
                loadingTimeoutJob = null
            }
        }

        val currentState = uiStateMutable.value
        val displayStations = if (stations.isNotEmpty()) {
            stations
        } else {
            currentState.lastSuccessfulStations.ifNotEmptyOr(currentState.stations)
        }

        uiStateMutable.value = currentState.copy(
            isLoading = displayStations.isEmpty() && !hasReceivedStations,
            stations = displayStations,
            savedStations = displayStations.filter { it.isFavorite },
            recentStations = resolveRecentStations(displayStations),
            lastSuccessfulStations = if (stations.isNotEmpty()) stations else currentState.lastSuccessfulStations,
            searchResults = if (currentState.searchQuery.isBlank()) {
                emptyList()
            } else {
                searchStations(displayStations, currentState.searchQuery)
            },
            quickAddEditor = currentState.quickAddEditor,
            stationEditor = currentState.stationEditor,
            errorMessage = null,
            networkErrorMessage = null
        )
        refreshDiscoveryRecommendations()
        recomputeListeningState()
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

    private suspend fun saveRadioStation(station: RadioStation): RadioStation {
        return if (radioRepository.getById(station.id) != null) {
            setRadioStationFavoriteUseCase(station.id, true)
        } else {
            saveRadioStationUseCase(station)
        }
    }

    private fun applySavedStation(savedStation: RadioStation) {
        latestStations = latestStations
            .map { station ->
                if (station.id == savedStation.id) savedStation else station
            }
            .ifEmpty { listOf(savedStation) }

        val currentState = uiStateMutable.value
        val refreshedStations = currentState.stations
            .map { station ->
                if (station.id == savedStation.id) savedStation else station
            }
            .ifEmpty { listOf(savedStation) }
        val refreshedSavedStations = refreshedStations.filter { it.isFavorite }
        val refreshedSearchResults = currentState.searchResults
            .map { station ->
                if (station.id == savedStation.id) savedStation else station
            }

        uiStateMutable.value = currentState.copy(
            stations = refreshedStations,
            savedStations = refreshedSavedStations,
            recentStations = resolveRecentStations(refreshedStations),
            lastSuccessfulStations = refreshedStations,
            recommendedStations = currentState.recommendedStations
                .map { station -> if (station.id == savedStation.id) savedStation else station }
                .filterNot { it.isFavorite },
            discoveryMoreStations = currentState.discoveryMoreStations
                .map { station -> if (station.id == savedStation.id) savedStation else station }
                .filterNot { it.isFavorite },
            searchResults = refreshedSearchResults,
            errorMessage = null,
            networkErrorMessage = null
        )
        refreshDiscoveryRecommendations()
        recomputeListeningState()
    }

    private fun refreshDiscoveryRecommendations() {
        val currentState = uiStateMutable.value
        val discoveryOptions = buildTaxonomyDiscoveryOptions(latestStations)
        val selectedType = currentState.selectedTaxonomyType
            ?.takeIf { it in discoveryOptions.typeOptions }
        val selectedRegion = currentState.selectedTaxonomyRegion
            ?.takeIf { it in discoveryOptions.regionOptions }
        val discoveryFeed = buildTaxonomyDiscoveryFeed(
            stations = latestStations,
            selectedType = selectedType,
            selectedRegion = selectedRegion
        )

        uiStateMutable.value = currentState.copy(
            recommendedStations = discoveryFeed.featuredStations,
            discoveryMoreStations = discoveryFeed.moreStations,
            taxonomyTypeOptions = discoveryOptions.typeOptions,
            taxonomyRegionOptions = discoveryOptions.regionOptions,
            selectedTaxonomyType = selectedType,
            selectedTaxonomyRegion = selectedRegion
        )
    }

    private fun announceAddSuccess(stationName: String) {
        uiStateMutable.value = uiStateMutable.value.copy(
            homeStatus = RadioHomeStatus.AddSuccess,
            addSuccessMessage = stationName,
            playbackErrorMessage = null,
            networkErrorMessage = null,
            errorMessage = null
        )

        viewModelScope.launch {
            delay(1_500)
            val currentState = uiStateMutable.value
            if (currentState.homeStatus == RadioHomeStatus.AddSuccess) {
                uiStateMutable.value = currentState.copy(
                    homeStatus = resolveStaticStatus(currentState),
                    addSuccessMessage = null
                )
                recomputeListeningState()
            }
        }
    }

    private fun setPlaybackFailure(message: String) {
        uiStateMutable.value = uiStateMutable.value.copy(
            homeStatus = RadioHomeStatus.PlaybackFailure,
            playbackErrorMessage = message,
            errorMessage = message
        )
    }

    private fun recomputeListeningState() {
        val currentState = uiStateMutable.value
        if (currentState.homeStatus == RadioHomeStatus.AddSuccess) return

        val nextStatus = when {
            currentState.playbackState is PlaybackStateModel.Error -> RadioHomeStatus.PlaybackFailure
            currentState.homeStatus == RadioHomeStatus.NetworkFailure &&
                currentState.stations.isEmpty() &&
                !currentState.isLoading -> RadioHomeStatus.NetworkFailure
            currentState.isLoading -> RadioHomeStatus.Loading
            currentState.playbackState == PlaybackStateModel.Playing &&
                currentState.currentItemId != null -> RadioHomeStatus.Playing
            currentState.playbackState == PlaybackStateModel.Buffering ||
                currentState.playbackState == PlaybackStateModel.Reconnecting ->
                if (currentState.currentItemId != null) RadioHomeStatus.Playing else resolveStaticStatus(currentState)
            else -> resolveStaticStatus(currentState)
        }

        uiStateMutable.value = currentState.copy(
            homeStatus = nextStatus,
            errorMessage = when (nextStatus) {
                RadioHomeStatus.NetworkFailure -> currentState.networkErrorMessage
                RadioHomeStatus.PlaybackFailure -> currentState.playbackErrorMessage
                else -> null
            }
        )
    }

    private fun resolveStaticStatus(state: RadioUiState): RadioHomeStatus {
        return when {
            state.stations.isEmpty() -> RadioHomeStatus.NetworkFailure
            state.savedStations.isEmpty() -> RadioHomeStatus.Empty
            else -> RadioHomeStatus.Ready
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

    private fun <T> List<T>.ifNotEmptyOr(fallback: List<T>): List<T> {
        return if (isNotEmpty()) this else fallback
    }

    private fun recordRecentStation(station: RadioStation) {
        recentStationIds.remove(station.id)
        recentStationIds.add(0, station.id)
        if (recentStationIds.size > 8) {
            recentStationIds.subList(8, recentStationIds.size).clear()
        }

        val currentState = uiStateMutable.value
        val recentStations = resolveRecentStations(currentState.stations.ifNotEmptyOr(latestStations))
        uiStateMutable.value = currentState.copy(
            recentStations = recentStations
        )
    }

    private fun resolveRecentStations(stations: List<RadioStation>): List<RadioStation> {
        val stationsById = stations.associateBy { it.id }
        return recentStationIds.mapNotNull { stationsById[it] }
    }

    private fun searchStations(
        stations: List<RadioStation>,
        query: String
    ): List<RadioStation> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return emptyList()

        return stations
            .filter { station ->
                station.name.contains(normalizedQuery, ignoreCase = true) ||
                    (station.displayName?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.displayFrequency?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.frequency?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.band?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.sourceGroup?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.network?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.region?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.category?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.mediaType?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.uiPrimaryGroup?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.uiSecondaryGroup?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    station.streamUrl.contains(normalizedQuery, ignoreCase = true) ||
                    (station.resolvedStreamUrl?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.country?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.countryCode?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.language?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.genre?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.codec?.contains(normalizedQuery, ignoreCase = true) == true) ||
                    (station.searchKeywords.any { it.contains(normalizedQuery, ignoreCase = true) }) ||
                    (station.aliases.any { it.contains(normalizedQuery, ignoreCase = true) }) ||
                    (station.mergedFromIds.any { it.contains(normalizedQuery, ignoreCase = true) }) ||
                    (station.bitrateKbps?.toString()?.contains(normalizedQuery, ignoreCase = true) == true)
            }
            .sortedWith(
                compareByDescending<RadioStation> { it.name.contains(normalizedQuery, ignoreCase = true) }
                    .thenByDescending { it.displayName?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.displayFrequency?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.network?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.region?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.country?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.language?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenByDescending { it.genre?.contains(normalizedQuery, ignoreCase = true) == true }
                    .thenBy { it.name.lowercase(Locale.ROOT) }
            )
    }

    private fun currentStation(state: RadioUiState): RadioStation? {
        val currentItemId = state.currentItemId ?: return null
        return findStationById(currentItemId, state.stations)
            ?: findStationById(currentItemId, state.savedStations)
            ?: findStationById(currentItemId, state.recentStations)
            ?: findStationById(currentItemId, latestStations)
    }

    private fun findStationById(id: String, stations: List<RadioStation>): RadioStation? {
        return stations.firstOrNull { it.id == id }
    }

    private fun PlaybackError.toDisplayMessage(): String {
        return when (this) {
            PlaybackError.Network -> "Network error."
            PlaybackError.SourceUnavailable -> "The station is unavailable."
            PlaybackError.UnsupportedFormat -> "This station format is not supported."
            PlaybackError.Unknown -> "Playback failed."
        }
    }
}
