package com.learpc.learpc.domain.usecase

import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.Test

class RadioPlaylistParserTest {
    @Test
    fun `parses pls playlist urls`() {
        val content = """
            [playlist]
            NumberOfEntries=1
            File1=https://example.com/stream.mp3
        """.trimIndent()

        assertEquals(
            "https://example.com/stream.mp3",
            RadioPlaylistParser.parse(content, "https://example.com/live.pls")
        )
    }

    @Test
    fun `parses m3u playlist urls`() {
        val content = """
            #EXTM3U
            #EXTINF:-1,Station
            https://example.com/stream.aac
        """.trimIndent()

        assertEquals(
            "https://example.com/stream.aac",
            RadioPlaylistParser.parse(content, "https://example.com/live.m3u")
        )
    }

    @Test
    fun `parses xspf playlist urls`() {
        val content = """
            <playlist>
              <trackList>
                <track>
                  <location>https://example.com/stream.ogg</location>
                </track>
              </trackList>
            </playlist>
        """.trimIndent()

        assertEquals(
            "https://example.com/stream.ogg",
            RadioPlaylistParser.parse(content, "https://example.com/live.xspf")
        )
    }

    @Test
    fun `returns null when playlist does not contain a playable url`() {
        val content = """
            [playlist]
            NumberOfEntries=1
        """.trimIndent()

        assertNull(RadioPlaylistParser.parse(content, "https://example.com/live.pls"))
    }
}
