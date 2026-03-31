package com.learpc.learpc.feature.radio.ui.model

import com.learpc.learpc.core.model.radio.RadioStation

data class RadioUiState(
    val isLoading: Boolean = true,
    val stations: List<RadioStation> = emptyList(),
    val errorMessage: String? = null
)
