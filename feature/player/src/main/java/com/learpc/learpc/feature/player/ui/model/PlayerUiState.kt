package com.learpc.learpc.feature.player.ui.model

import com.learpc.learpc.core.model.PlayableItem

data class PlayerUiState(
    val currentItem: PlayableItem? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false
)
