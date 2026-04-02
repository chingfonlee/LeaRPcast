package com.learpc.learpc.feature.radio.ui.model

import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.feature.radio.R

enum class RadioShellTab {
    Home,
    Stations,
    Search,
    Favorites,
    Player
}

enum class RadioBrowseSortMode {
    Recent,
    Name,
    Frequency
}

enum class RadioStationsTaxonomyMode {
    Categories,
    Networks,
    Regions
}

val RadioBrowseSortMode.labelResId: Int
    get() = when (this) {
        RadioBrowseSortMode.Recent -> R.string.radio_shell_sort_recent
        RadioBrowseSortMode.Name -> R.string.radio_shell_sort_name
        RadioBrowseSortMode.Frequency -> R.string.radio_shell_sort_frequency
    }

val RadioShellTab.labelResId: Int
    get() = when (this) {
        RadioShellTab.Home -> R.string.radio_shell_tab_home
        RadioShellTab.Stations -> R.string.radio_shell_tab_stations
        RadioShellTab.Search -> R.string.radio_shell_tab_search
        RadioShellTab.Favorites -> R.string.radio_shell_tab_favorites
        RadioShellTab.Player -> R.string.radio_shell_tab_player
    }

val RadioStationsTaxonomyMode.labelResId: Int
    get() = when (this) {
        RadioStationsTaxonomyMode.Categories -> R.string.radio_shell_stations_mode_categories
        RadioStationsTaxonomyMode.Networks -> R.string.radio_shell_stations_mode_networks
        RadioStationsTaxonomyMode.Regions -> R.string.radio_shell_stations_mode_regions
    }

enum class RadioHomeStatus {
    Loading,
    Ready,
    Empty,
    Playing,
    AddSuccess,
    PlaybackFailure,
    NetworkFailure
}

data class RadioUiState(
    val selectedShellTab: RadioShellTab = RadioShellTab.Home,
    val browseSortMode: RadioBrowseSortMode = RadioBrowseSortMode.Recent,
    val stationsTaxonomyMode: RadioStationsTaxonomyMode = RadioStationsTaxonomyMode.Categories,
    val searchQuery: String = "",
    val homeStatus: RadioHomeStatus = RadioHomeStatus.Loading,
    val isLoading: Boolean = true,
    val stations: List<RadioStation> = emptyList(),
    val savedStations: List<RadioStation> = emptyList(),
    val recentStations: List<RadioStation> = emptyList(),
    val currentItemId: String? = null,
    val playbackState: PlaybackStateModel = PlaybackStateModel.Idle,
    val addSuccessMessage: String? = null,
    val playbackErrorMessage: String? = null,
    val networkErrorMessage: String? = null,
    val lastSuccessfulStations: List<RadioStation> = emptyList(),
    val recommendedStations: List<RadioStation> = emptyList(),
    val discoveryMoreStations: List<RadioStation> = emptyList(),
    val taxonomyTypeOptions: List<String> = emptyList(),
    val taxonomyRegionOptions: List<String> = emptyList(),
    val selectedTaxonomyType: String? = null,
    val selectedTaxonomyRegion: String? = null,
    val searchResults: List<RadioStation> = emptyList(),
    val discoveryQuery: String = "",
    val isDiscoverySearchExpanded: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
    val quickAddEditor: RadioQuickAddState? = null,
    val stationEditor: RadioStationEditorState? = null
)

data class RadioQuickAddState(
    val name: String = "",
    val streamUrl: String = "",
    val validationError: String? = null
)

data class RadioStationEditorState(
    val stationId: String? = null,
    val name: String = "",
    val streamUrl: String = "",
    val resolvedStreamUrl: String = "",
    val homepageUrl: String = "",
    val artworkUrl: String = "",
    val country: String = "",
    val language: String = "",
    val genre: String = "",
    val validationError: String? = null
)
