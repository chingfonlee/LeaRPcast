package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.PodcastEntity
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.network.rss.model.RemoteFeedPodcast

object PodcastMapper {
    fun PodcastEntity.toDomain(): Podcast {
        return Podcast(
            id = podcastId,
            feedUrl = feedUrl,
            title = title,
            author = author,
            description = description,
            websiteUrl = websiteUrl,
            artworkUrl = artworkUrl,
            language = language,
            copyright = copyright,
            explicit = explicit,
            lastFeedPublishedAt = lastFeedPublishedAt,
            lastRefreshedAt = lastRefreshedAt,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    fun Podcast.toEntity(): PodcastEntity {
        return PodcastEntity(
            podcastId = id,
            feedUrl = feedUrl,
            title = title,
            author = author,
            description = description,
            websiteUrl = websiteUrl,
            artworkUrl = artworkUrl,
            language = language,
            copyright = copyright,
            explicit = explicit,
            lastFeedPublishedAt = lastFeedPublishedAt,
            lastRefreshedAt = lastRefreshedAt,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    fun RemoteFeedPodcast.toEntity(
        feedUrl: String,
        createdAt: Long = System.currentTimeMillis(),
        updatedAt: Long = createdAt,
        podcastId: String = feedUrl,
        lastFeedPublishedAt: Long? = episodes.mapNotNull { it.pubDate.toEpochMillisOrNull() }.maxOrNull()
    ): PodcastEntity {
        return PodcastEntity(
            podcastId = podcastId,
            feedUrl = feedUrl,
            title = title,
            author = null,
            description = description,
            websiteUrl = null,
            artworkUrl = artworkUrl,
            language = null,
            copyright = null,
            explicit = false,
            lastFeedPublishedAt = lastFeedPublishedAt,
            lastRefreshedAt = updatedAt,
            isActive = true,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun String?.toEpochMillisOrNull(): Long? {
        if (isNullOrBlank()) return null

        return runCatching {
            java.time.ZonedDateTime.parse(
                this,
                java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
            ).toInstant().toEpochMilli()
        }.getOrNull()
    }
}
