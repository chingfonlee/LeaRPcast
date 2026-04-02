package com.learpc.learpc.feature.radio.ui.viewmodel

import com.learpc.learpc.core.model.radio.RadioStation
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RadioDiscoveryFeedTest {
    @Test
    fun `buildDiscoveryFeed keeps discovery useful without precise locale access`() {
        val stations = listOf(
            station(id = "charlie", name = "Charlie", sortOrder = 2),
            station(id = "alpha", name = "Alpha", sortOrder = 0),
            station(id = "bravo", name = "Bravo", sortOrder = 1),
            station(id = "delta", name = "Delta", sortOrder = 3)
        )

        val feed = buildDiscoveryFeed(
            stations = stations,
            locale = Locale("", "")
        )

        assertEquals(listOf("alpha", "bravo", "charlie", "delta"), feed.featuredStations.map { it.id })
        assertTrue(feed.moreStations.isEmpty())
    }

    @Test
    fun `buildDiscoveryFeed provides a second row when enough stations exist`() {
        val stations = listOf(
            station(id = "alpha", name = "Alpha", sortOrder = 0),
            station(id = "bravo", name = "Bravo", sortOrder = 1),
            station(id = "charlie", name = "Charlie", sortOrder = 2),
            station(id = "delta", name = "Delta", sortOrder = 3),
            station(id = "echo", name = "Echo", sortOrder = 4)
        )

        val feed = buildDiscoveryFeed(
            stations = stations,
            locale = Locale("", "")
        )

        assertEquals(listOf("alpha", "bravo", "charlie", "delta"), feed.featuredStations.map { it.id })
        assertEquals(listOf("echo"), feed.moreStations.map { it.id })
    }

    @Test
    fun `buildDiscoveryOptions surfaces common type and region buttons`() {
        val stations = listOf(
            station(id = "news-1", name = "News One", genre = "News", country = "Japan"),
            station(id = "news-2", name = "News Two", genre = "News", country = "Japan"),
            station(id = "jazz-1", name = "Jazz One", genre = "Jazz", country = "Taiwan"),
            station(id = "talk-1", name = "Talk One", genre = "Talk", country = "Taiwan"),
            station(id = "pop-1", name = "Pop One", genre = "Pop", country = "United States")
        )

        val options = buildDiscoveryOptions(stations, limit = 3)

        assertEquals(listOf("News", "Jazz", "Pop"), options.typeOptions)
        assertEquals(listOf("Japan", "Taiwan", "United States"), options.regionOptions)
    }

    @Test
    fun `buildDiscoveryFeed boosts selected type and region matches`() {
        val stations = listOf(
            station(id = "jazz-tw", name = "Taiwan Jazz", genre = "Jazz", country = "Taiwan", sortOrder = 2),
            station(id = "pop-jp", name = "Japan Pop", genre = "Pop", country = "Japan", sortOrder = 0),
            station(id = "news-us", name = "US News", genre = "News", country = "United States", sortOrder = 1)
        )

        val feed = buildDiscoveryFeed(
            stations = stations,
            locale = Locale("", ""),
            selectedType = "Jazz",
            selectedRegion = "Taiwan"
        )

        assertEquals(listOf("jazz-tw", "pop-jp", "news-us"), feed.featuredStations.map { it.id })
    }

    private fun station(
        id: String,
        name: String,
        sortOrder: Int = 0,
        genre: String? = null,
        country: String? = null
    ): RadioStation {
        val now = 1_700_000_000_000L
        return RadioStation(
            id = id,
            name = name,
            streamUrl = "https://example.com/$id.mp3",
            genre = genre,
            country = country,
            sortOrder = sortOrder,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )
    }
}
