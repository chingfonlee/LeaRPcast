package com.learpc.learpc.feature.podcast.ui.model

import com.learpc.learpc.core.model.podcast.Episode

data class EpisodeUiState(
    val isLoading: Boolean = true,
    val episodes: List<Episode> = emptyList(),
    val currentlyPlayingId: String? = null,
    val errorMessage: String? = null
)
