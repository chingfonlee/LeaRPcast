package com.learpc.learpc.feature.radio.ui.model

import com.learpc.learpc.core.model.radio.RadioStation

data class RadioUiState(
    val isLoading: Boolean = true,
    val stations: List<RadioStation> = emptyList(),
    val errorMessage: String? = null,
    val stationEditor: RadioStationEditorState? = null
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
