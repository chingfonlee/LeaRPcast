package com.learpc.learpc.core.model

sealed interface PlaybackStateModel {
    data object Idle : PlaybackStateModel
    data object Buffering : PlaybackStateModel
    data object Reconnecting : PlaybackStateModel
    data object Playing : PlaybackStateModel
    data object Paused : PlaybackStateModel
    data class Error(val error: PlaybackError) : PlaybackStateModel
}
