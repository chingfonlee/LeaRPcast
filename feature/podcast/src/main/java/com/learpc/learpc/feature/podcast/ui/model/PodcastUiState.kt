package com.learpc.learpc.feature.podcast.ui.model

import com.learpc.learpc.core.model.podcast.Podcast

data class PodcastUiState(
    val isLoading: Boolean = true,
    val podcasts: List<Podcast> = emptyList(),
    val errorMessage: String? = null
)
