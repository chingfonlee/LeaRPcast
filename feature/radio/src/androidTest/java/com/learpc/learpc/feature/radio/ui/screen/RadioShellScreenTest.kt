package com.learpc.learpc.feature.radio.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.learpc.learpc.core.model.PlaybackError
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.feature.radio.ui.model.RadioBrowseSortMode
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import com.learpc.learpc.feature.radio.ui.model.RadioShellTab
import com.learpc.learpc.feature.radio.ui.model.RadioStationsBrowseMode
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RadioShellScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun tapping_station_updates_now_playing_strip() {
        var selectedTab by mutableStateOf(RadioShellTab.Home)
        var searchQuery by mutableStateOf("")
        var browseSortMode by mutableStateOf(RadioBrowseSortMode.Recent)
        var stationsBrowseMode by mutableStateOf(RadioStationsBrowseMode.Categories)
        var currentItemId by mutableStateOf<String?>(null)
        var playbackState by mutableStateOf<PlaybackStateModel>(PlaybackStateModel.Idle)
        var stations by mutableStateOf(
            listOf(
                station("morning-fm", "Morning FM", genre = "News", country = "Taiwan"),
                station("night-fm", "Night FM", genre = "Music", country = "Japan")
            )
        )

        composeRule.setContent {
            RadioShellScreen(
                uiState = RadioUiState(
                    selectedShellTab = selectedTab,
                    browseSortMode = browseSortMode,
                    stationsBrowseMode = stationsBrowseMode,
                    searchQuery = searchQuery,
                    homeStatus = RadioHomeStatus.Ready,
                    isLoading = false,
                    stations = stations,
                    savedStations = stations.filter { it.isFavorite },
                    recentStations = stations.take(1),
                    currentItemId = currentItemId,
                    playbackState = playbackState,
                    searchResults = stations.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }
                ),
                onShellTabSelected = { selectedTab = it },
                onPlayStation = { station ->
                    currentItemId = station.id
                    playbackState = PlaybackStateModel.Playing
                },
                onToggleFavorite = { station ->
                    stations = stations.map {
                        if (it.id == station.id) it.copy(isFavorite = !it.isFavorite) else it
                    }
                },
                onAddStation = {},
                onRetryLoad = {},
                onSearchQueryChange = { searchQuery = it },
                onBrowseSortModeChange = { browseSortMode = it },
                onStationsBrowseModeChange = { stationsBrowseMode = it },
                onRetryPlayback = {
                    playbackState = PlaybackStateModel.Buffering
                },
                onTogglePlayback = {
                    playbackState = if (playbackState == PlaybackStateModel.Playing) {
                        PlaybackStateModel.Paused
                    } else {
                        PlaybackStateModel.Playing
                    }
                }
            )
        }

        composeRule.onNodeWithText("Stations").performClick()
        composeRule.onNodeWithText("Categories").performClick()
        composeRule.onNodeWithText("Networks").performClick()
        composeRule.onNodeWithText("Regions").performClick()
        composeRule.onNodeWithText("Morning FM").performClick()

        composeRule.onNodeWithText("Now playing").assertIsDisplayed()
        composeRule.onNodeWithText("Morning FM").assertIsDisplayed()
        assertEquals(RadioStationsBrowseMode.Regions, stationsBrowseMode)
        assertEquals("morning-fm", currentItemId)
    }

    @Test
    fun search_tab_filters_and_shows_empty_state() {
        var selectedTab by mutableStateOf(RadioShellTab.Home)
        var searchQuery by mutableStateOf("")
        val stations = listOf(
            station("morning-fm", "Morning FM", genre = "News", country = "Taiwan"),
            station("night-fm", "Night FM", genre = "Music", country = "Japan")
        )

        composeRule.setContent {
            RadioShellScreen(
                uiState = RadioUiState(
                    selectedShellTab = selectedTab,
                    stationsBrowseMode = RadioStationsBrowseMode.Categories,
                    searchQuery = searchQuery,
                    homeStatus = RadioHomeStatus.Ready,
                    isLoading = false,
                    stations = stations,
                    savedStations = emptyList(),
                    recentStations = emptyList(),
                    searchResults = stations.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }
                ),
                onShellTabSelected = { selectedTab = it },
                onPlayStation = {},
                onToggleFavorite = {},
                onAddStation = {},
                onRetryLoad = {},
                onSearchQueryChange = { searchQuery = it },
                onBrowseSortModeChange = {},
                onStationsBrowseModeChange = {},
                onRetryPlayback = {},
                onTogglePlayback = {}
            )
        }

        composeRule.onNodeWithText("Search").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("Night")
        composeRule.onNodeWithText("Night FM").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("Morning FM").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun favorites_empty_state_and_player_failure_are_visible() {
        var selectedTab by mutableStateOf(RadioShellTab.Favorites)
        var playbackState by mutableStateOf<PlaybackStateModel>(
            PlaybackStateModel.Error(PlaybackError.Network)
        )
        val stations = listOf(
            station("morning-fm", "Morning FM", genre = "News", country = "Taiwan")
        )

        composeRule.setContent {
            RadioShellScreen(
                uiState = RadioUiState(
                    selectedShellTab = selectedTab,
                    stationsBrowseMode = RadioStationsBrowseMode.Categories,
                    homeStatus = RadioHomeStatus.PlaybackFailure,
                    isLoading = false,
                    stations = stations,
                    savedStations = emptyList(),
                    recentStations = emptyList(),
                    currentItemId = stations.first().id,
                    playbackState = playbackState
                ),
                onShellTabSelected = { selectedTab = it },
                onPlayStation = {},
                onToggleFavorite = {},
                onAddStation = {},
                onRetryLoad = {},
                onSearchQueryChange = {},
                onBrowseSortModeChange = {},
                onStationsBrowseModeChange = {},
                onRetryPlayback = {
                    playbackState = PlaybackStateModel.Buffering
                },
                onTogglePlayback = {}
            )
        }

        composeRule.onNodeWithText("Your favorites are empty").assertIsDisplayed()
        composeRule.onNodeWithText("Browse stations").performClick()
        assertEquals(RadioShellTab.Stations, selectedTab)

        composeRule.onNodeWithText("Player").performClick()
        composeRule.onNodeWithText("Playback failed").assertIsDisplayed()
        composeRule.onNodeWithText("Play again").assertIsDisplayed()
    }

    private fun station(
        id: String,
        name: String,
        genre: String? = null,
        country: String? = null,
        isFavorite: Boolean = false
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            country = country,
            language = "zh",
            genre = genre,
            isFavorite = isFavorite,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
