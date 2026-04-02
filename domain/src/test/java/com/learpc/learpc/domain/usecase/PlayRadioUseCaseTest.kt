package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.mapper.RadioPlayableMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.just
import io.mockk.Runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayRadioUseCaseTest {
    private val playbackController = mockk<PlaybackController>()
    private val radioPlayableMapper = RadioPlayableMapper()
    private val resolveRadioStreamUseCase = mockk<ResolveRadioStreamUseCase>()

    private val useCase = PlayRadioUseCase(
        playbackController = playbackController,
        radioPlayableMapper = radioPlayableMapper,
        resolveRadioStreamUseCase = resolveRadioStreamUseCase
    )

    @Test
    fun `uses resolved stream and fallback when available`() = runTest {
        val station = sampleStation(
            streamUrl = "https://example.com/live.pls",
            resolvedStreamUrl = "https://example.com/live.mp3"
        )
        val itemSlot = slot<PlayableItem>()

        every { playbackController.setItem(capture(itemSlot)) } just Runs
        every { playbackController.play() } just Runs
        coEvery { resolveRadioStreamUseCase(station) } returns ResolvedRadioStream(
            mediaUri = "https://example.com/live.mp3",
            fallbackMediaUri = "https://example.com/live.pls"
        )

        useCase(station)

        assertEquals("https://example.com/live.mp3", itemSlot.captured.mediaUri)
        assertEquals("https://example.com/live.pls", itemSlot.captured.fallbackMediaUri)
        verify(exactly = 1) { playbackController.play() }
    }

    private fun sampleStation(
        streamUrl: String,
        resolvedStreamUrl: String?
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = "station-1",
            name = "Station 1",
            streamUrl = streamUrl,
            resolvedStreamUrl = resolvedStreamUrl,
            homepageUrl = null,
            artworkUrl = null,
            country = null,
            countryCode = null,
            language = null,
            genre = null,
            codec = null,
            bitrateKbps = null,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
