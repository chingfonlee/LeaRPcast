package com.learpc.learpc.core.media

import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import com.learpc.learpc.core.model.PlaybackError
import com.learpc.learpc.core.model.PlaybackStateModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerEventMapperTest {
    private val mapper = PlayerEventMapper()

    @Test
    fun `maps idle state to idle`() {
        assertEquals(
            PlaybackStateModel.Idle,
            mapper.mapState(Player.STATE_IDLE, playWhenReady = false, error = null)
        )
    }

    @Test
    fun `maps buffering state to buffering`() {
        assertEquals(
            PlaybackStateModel.Buffering,
            mapper.mapState(Player.STATE_BUFFERING, playWhenReady = false, error = null)
        )
    }

    @Test
    fun `maps ready and playing to playing`() {
        assertEquals(
            PlaybackStateModel.Playing,
            mapper.mapState(Player.STATE_READY, playWhenReady = true, error = null)
        )
    }

    @Test
    fun `maps ready and paused to paused`() {
        assertEquals(
            PlaybackStateModel.Paused,
            mapper.mapState(Player.STATE_READY, playWhenReady = false, error = null)
        )
    }

    @Test
    fun `maps ended state to idle`() {
        assertEquals(
            PlaybackStateModel.Idle,
            mapper.mapState(Player.STATE_ENDED, playWhenReady = false, error = null)
        )
    }

    @Test
    fun `maps playback error to domain error`() {
        assertEquals(
            PlaybackStateModel.Error(PlaybackError.Network),
            PlaybackStateModel.Error(
                mapper.mapPlaybackError(
                    PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                )
            )
        )
    }
}
