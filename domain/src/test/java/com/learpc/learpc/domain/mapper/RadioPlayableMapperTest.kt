package com.learpc.learpc.domain.mapper

import com.learpc.learpc.core.model.radio.RadioStation
import org.junit.Assert.assertEquals
import org.junit.Test

class RadioPlayableMapperTest {
    private val mapper = RadioPlayableMapper()

    @Test
    fun `prefers resolved stream url for playback`() {
        val station = radioStation(
            streamUrl = "https://example.com/raw.mp3",
            resolvedStreamUrl = "https://example.com/resolved.mp3"
        )

        val playableItem = mapper.toPlayableItem(station)

        assertEquals("https://example.com/resolved.mp3", playableItem.mediaUri)
    }

    @Test
    fun `falls back to raw stream url when resolved url is missing`() {
        val station = radioStation(
            streamUrl = "https://example.com/raw.mp3",
            resolvedStreamUrl = null
        )

        val playableItem = mapper.toPlayableItem(station)

        assertEquals("https://example.com/raw.mp3", playableItem.mediaUri)
    }

    private fun radioStation(
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
