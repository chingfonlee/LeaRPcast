package com.learpc.learpc.feature.radio.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
import org.junit.Rule
import org.junit.Test

class RadioHomeContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shows_listening_first_surface() {
        val station = station("station-1", "Morning FM", isFavorite = true)

        composeRule.setContent {
            RadioHomeContent(
                uiState = RadioUiState(
                    homeStatus = RadioHomeStatus.Ready,
                    isLoading = false,
                    stations = listOf(station),
                    savedStations = listOf(station),
                    currentItemId = station.id,
                    playbackState = PlaybackStateModel.Playing
                ),
                onPlayStation = {},
                onToggleFavorite = {},
                onAddStation = {},
                onRetryLoad = {},
                onQuickAddChange = {},
                onDismissQuickAdd = {},
                onSaveQuickAdd = {},
                onStationEditorChange = {},
                onDismissStationEditor = {},
                onSaveStation = {}
            )
        }

        composeRule.onNodeWithText("Radio").assertExists()
        composeRule.onNodeWithText("Add favorite station").assertExists()
        composeRule.onNodeWithText("Morning FM").assertExists()
    }

    private fun station(
        id: String,
        name: String,
        isFavorite: Boolean
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            isFavorite = isFavorite,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
