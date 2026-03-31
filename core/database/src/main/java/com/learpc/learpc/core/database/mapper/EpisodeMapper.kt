package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.EpisodeEntity
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.network.rss.model.RemoteFeedEpisode

object EpisodeMapper {
    fun EpisodeEntity.toDomain(): Episode {
        return Episode(
            id = episodeId,
            podcastId = podcastId,
            title = title,
            mediaUrl = mediaUrl,
            localFileUri = null,
            publishedAt = publishedAt,
            durationMs = durationMs,
            playbackPositionMs = 0L,
            isPlayed = isPlayed,
            isDownloaded = isDownloaded,
            downloadStatus = when {
                isDownloaded -> DownloadStatus.COMPLETED
                isDownloading -> DownloadStatus.DOWNLOADING
                else -> null
            },
            keepDownload = keepDownload
        )
    }

    fun Episode.toEntity(): EpisodeEntity {
        val now = System.currentTimeMillis()
        return EpisodeEntity(
            episodeId = id,
            podcastId = podcastId,
            guid = id.takeIf { it.isNotBlank() },
            title = title,
            description = null,
            summary = null,
            mediaUrl = mediaUrl,
            mimeType = null,
            durationMs = durationMs,
            fileSizeBytes = null,
            publishedAt = publishedAt,
            artworkUrl = null,
            seasonNumber = null,
            episodeNumber = null,
            explicit = false,
            isPlayed = isPlayed,
            playedAt = null,
            isArchived = false,
            archivedAt = null,
            isDownloaded = isDownloaded,
            isDownloading = downloadStatus == DownloadStatus.DOWNLOADING,
            isFavorite = false,
            keepDownload = keepDownload,
            createdAt = now,
            updatedAt = now
        )
    }

    fun RemoteFeedEpisode.toEntity(
        podcastId: String,
        createdAt: Long = System.currentTimeMillis(),
        updatedAt: Long = createdAt
    ): EpisodeEntity {
        val stableId = buildString {
            append(podcastId)
            append(':')
            append(guid.ifBlank { audioUrl })
        }

        return EpisodeEntity(
            episodeId = stableId,
            podcastId = podcastId,
            guid = guid.takeIf { it.isNotBlank() },
            title = title,
            description = null,
            summary = null,
            mediaUrl = audioUrl,
            mimeType = null,
            durationMs = duration?.toDurationMillisOrNull(),
            fileSizeBytes = null,
            publishedAt = pubDate.toEpochMillisOrNull(),
            artworkUrl = null,
            seasonNumber = null,
            episodeNumber = null,
            explicit = false,
            isPlayed = false,
            playedAt = null,
            isArchived = false,
            archivedAt = null,
            isDownloaded = false,
            isDownloading = false,
            isFavorite = false,
            keepDownload = false,
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
