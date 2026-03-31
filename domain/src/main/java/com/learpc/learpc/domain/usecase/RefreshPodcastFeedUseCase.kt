package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.database.source.EpisodeLocalDataSource
import com.learpc.learpc.core.database.source.PodcastLocalDataSource
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.network.rss.source.PodcastRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RefreshPodcastFeedUseCase @Inject constructor(
    private val podcastRemoteDataSource: PodcastRemoteDataSource,
    private val podcastLocalDataSource: PodcastLocalDataSource,
    private val episodeLocalDataSource: EpisodeLocalDataSource
) {
    suspend operator fun invoke(
        podcastId: String,
        feedUrl: String
    ): Result<Unit> {
        return runCatching {
            val remoteFeed = podcastRemoteDataSource.fetchFeed(feedUrl).getOrThrow()
            val now = System.currentTimeMillis()
            val existingPodcast = podcastLocalDataSource.getById(podcastId)

            podcastLocalDataSource.upsert(
                Podcast(
                    id = podcastId,
                    feedUrl = feedUrl,
                    title = remoteFeed.title,
                    author = existingPodcast?.author,
                    description = remoteFeed.description,
                    websiteUrl = existingPodcast?.websiteUrl,
                    artworkUrl = remoteFeed.artworkUrl,
                    language = existingPodcast?.language,
                    copyright = existingPodcast?.copyright,
                    explicit = existingPodcast?.explicit ?: false,
                    lastFeedPublishedAt = remoteFeed.episodes.mapNotNull { it.pubDate.toEpochMillisOrNull() }.maxOrNull(),
                    lastRefreshedAt = now,
                    isActive = existingPodcast?.isActive ?: true,
                    createdAt = existingPodcast?.createdAt ?: now,
                    updatedAt = now
                )
            )

            val existingEpisodesById = remoteFeed.episodes
                .associateBy { episodeIdFor(podcastId, it.guid, it.audioUrl) }

            withContext(Dispatchers.IO) {
                existingEpisodesById.values.forEach { remoteEpisode ->
                    val episodeId = episodeIdFor(podcastId, remoteEpisode.guid, remoteEpisode.audioUrl)
                    val existingEpisode = episodeLocalDataSource.getById(episodeId)

                    episodeLocalDataSource.upsert(
                        Episode(
                            id = episodeId,
                            podcastId = podcastId,
                            title = remoteEpisode.title,
                            mediaUrl = remoteEpisode.audioUrl,
                            localFileUri = existingEpisode?.localFileUri,
                            publishedAt = remoteEpisode.pubDate.toEpochMillisOrNull(),
                            durationMs = remoteEpisode.duration.toDurationMillisOrNull(),
                            playbackPositionMs = existingEpisode?.playbackPositionMs ?: 0L,
                            isPlayed = existingEpisode?.isPlayed ?: false,
                            isDownloaded = existingEpisode?.isDownloaded ?: false,
                            downloadStatus = existingEpisode?.downloadStatus,
                            keepDownload = existingEpisode?.keepDownload ?: false
                        )
                    )
                }
            }
        }
    }

    private fun episodeIdFor(podcastId: String, guid: String, audioUrl: String): String {
        val stableKey = guid.ifBlank { audioUrl }
        return "$podcastId:$stableKey"
    }

    private fun String?.toEpochMillisOrNull(): Long? {
        if (isNullOrBlank()) return null

        return runCatching {
            ZonedDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME)
                .toInstant()
                .toEpochMilli()
        }.getOrNull()
    }

    private fun String?.toDurationMillisOrNull(): Long? {
        if (isNullOrBlank()) return null

        val parts = split(":")
        return when (parts.size) {
            1 -> parts[0].toLongOrNull()?.times(1_000L)
            2 -> {
                val minutes = parts[0].toLongOrNull() ?: return null
                val seconds = parts[1].toLongOrNull() ?: return null
                (minutes * 60 + seconds) * 1_000L
            }
            3 -> {
                val hours = parts[0].toLongOrNull() ?: return null
                val minutes = parts[1].toLongOrNull() ?: return null
                val seconds = parts[2].toLongOrNull() ?: return null
                (hours * 3_600 + minutes * 60 + seconds) * 1_000L
            }
            else -> null
        }
    }
}
