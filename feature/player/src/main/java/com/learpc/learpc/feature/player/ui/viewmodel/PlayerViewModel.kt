package com.learpc.learpc.feature.player.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.feature.player.ui.model.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackController: PlaybackController
) : ViewModel() {
    val uiState: StateFlow<PlayerUiState> = combine(
        playbackController.playbackState,
        playbackController.currentItem
    ) { playbackState, currentItem ->
        PlayerUiState(
            currentItem = currentItem,
            isPlaying = playbackState == PlaybackStateModel.Playing,
            isBuffering = playbackState == PlaybackStateModel.Buffering ||
                playbackState == PlaybackStateModel.Reconnecting
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlayerUiState()
    )

    fun playOrPause() {
        if (uiState.value.isPlaying) {
            playbackController.pause()
        } else {
            playbackController.play()
        }
    }
}
