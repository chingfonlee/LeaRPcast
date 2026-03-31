package com.learpc.learpc.core.media

import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.PlaybackStateModel
import androidx.media3.session.MediaController
import kotlinx.coroutines.flow.StateFlow

interface PlaybackController {
    val playbackState: StateFlow<PlaybackStateModel>
    val currentItem: StateFlow<PlayableItem?>

    fun connectController(mediaController: MediaController)
    fun setItem(item: PlayableItem)
    fun updatePlaybackState(playbackState: PlaybackStateModel)

    fun play()

    fun pause()

    fun seekTo(positionMs: Long)
}
