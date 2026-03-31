package com.learpc.learpc.core.network.rss

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedParserTest {
    private val parser = FeedParser()

    @Test
    fun `parse extracts podcast metadata and episodes`() {
        val result = parser.parse(SAMPLE_RSS)

        assertTrue(result.isSuccess)

        val feed = result.getOrThrow()
        assertEquals("Sample Podcast", feed.title)
        assertEquals("A demo feed for testing.", feed.description)
        assertEquals("https://example.com/artwork.jpg", feed.artworkUrl)
        assertEquals(1, feed.episodes.size)

        val episode = feed.episodes.first()
        assertEquals("episode-1", episode.guid)
        assertEquals("Episode One", episode.title)
        assertEquals("https://example.com/audio/episode-1.mp3", episode.audioUrl)
        assertEquals("01:02:03", episode.duration)
        assertEquals("Tue, 02 Jan 2024 10:00:00 GMT", episode.pubDate)
    }

    private companion object {
        const val SAMPLE_RSS = """
            <rss version="2.0" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
              <channel>
                <title>Sample Podcast</title>
                <description>A demo feed for testing.</description>
                <itunes:image href="https://example.com/artwork.jpg" />
                <item>
                  <guid>episode-1</guid>
                  <title>Episode One</title>
                  <enclosure url="https://example.com/audio/episode-1.mp3" length="1234" type="audio/mpeg" />
                  <itunes:duration>01:02:03</itunes:duration>
                  <pubDate>Tue, 02 Jan 2024 10:00:00 GMT</pubDate>
                </item>
              </channel>
            </rss>
        """
    }
}
