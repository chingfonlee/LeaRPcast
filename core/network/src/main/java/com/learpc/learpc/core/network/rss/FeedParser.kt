package com.learpc.learpc.core.network.rss

import com.learpc.learpc.core.network.rss.model.RemoteFeedEpisode
import com.learpc.learpc.core.network.rss.model.RemoteFeedPodcast
import org.kxml2.io.KXmlParser
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import javax.inject.Inject

class FeedParser @Inject constructor() {
    fun parse(xml: String): Result<RemoteFeedPodcast> {
        return runCatching {
            val parser = KXmlParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
            parser.setInput(StringReader(xml))

            advanceToStartTag(parser)
            parser.require(XmlPullParser.START_TAG, null, "rss")

            var title = ""
            var description: String? = null
            var artworkUrl: String? = null
            val episodes = mutableListOf<RemoteFeedEpisode>()

            while (parser.next() != XmlPullParser.END_TAG || parser.name != "rss") {
                if (parser.eventType != XmlPullParser.START_TAG) continue

                when (parser.name) {
                    "channel" -> {
                        val channel = parseChannel(parser)
                        title = channel.title
                        description = channel.description
                        artworkUrl = channel.artworkUrl
                        episodes.addAll(channel.episodes)
                    }
                    else -> skip(parser)
                }
            }

            RemoteFeedPodcast(
                title = title,
                description = description,
                artworkUrl = artworkUrl,
                episodes = episodes
            )
        }
    }

    private fun parseChannel(parser: XmlPullParser): RemoteFeedPodcast {
        parser.require(XmlPullParser.START_TAG, null, "channel")

        var title = ""
        var description: String? = null
        var artworkUrl: String? = null
        val episodes = mutableListOf<RemoteFeedEpisode>()

        while (parser.next() != XmlPullParser.END_TAG || parser.name != "channel") {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "title" -> title = parser.nextText().trim()
                "description" -> description = parser.nextText().trim().takeIf { it.isNotEmpty() }
                "image" -> {
                    val href = parser.getAttributeValue(null, "href")?.trim()
                    if (!href.isNullOrEmpty()) {
                        artworkUrl = href
                    }
                    skip(parser)
                }
                "item" -> episodes += parseItem(parser)
                else -> skip(parser)
            }
        }

        return RemoteFeedPodcast(
            title = title,
            description = description,
            artworkUrl = artworkUrl,
            episodes = episodes
        )
    }

    private fun parseItem(parser: XmlPullParser): RemoteFeedEpisode {
        parser.require(XmlPullParser.START_TAG, null, "item")

        var guid = ""
        var title = ""
        var audioUrl = ""
        var duration: String? = null
        var pubDate: String? = null

        while (parser.next() != XmlPullParser.END_TAG || parser.name != "item") {
            if (parser.eventType != XmlPullParser.START_TAG) continue

            when (parser.name) {
                "guid" -> guid = parser.nextText().trim()
                "title" -> title = parser.nextText().trim()
                "enclosure" -> {
                    audioUrl = parser.getAttributeValue(null, "url")?.trim().orEmpty()
                    skip(parser)
                }
                "duration" -> duration = parser.nextText().trim().takeIf { it.isNotEmpty() }
                "pubDate" -> pubDate = parser.nextText().trim().takeIf { it.isNotEmpty() }
                else -> skip(parser)
            }
        }

        return RemoteFeedEpisode(
            guid = guid,
            title = title,
            audioUrl = audioUrl,
            duration = duration,
            pubDate = pubDate
        )
    }

    private fun advanceToStartTag(parser: XmlPullParser) {
        while (parser.eventType != XmlPullParser.START_TAG &&
            parser.eventType != XmlPullParser.END_DOCUMENT
        ) {
            parser.next()
        }
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) return

        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
            }
        }
    }
}
