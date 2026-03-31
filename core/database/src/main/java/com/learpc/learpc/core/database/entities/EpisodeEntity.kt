package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "episode",
    foreignKeys = [
        ForeignKey(
            entity = PodcastEntity::class,
            parentColumns = ["podcast_id"],
            childColumns = ["podcast_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["podcast_id"]),
        Index(value = ["guid"]),
        Index(value = ["podcast_id", "published_at"]),
        Index(value = ["podcast_id", "is_played"]),
        Index(value = ["podcast_id", "is_archived"]),
        Index(value = ["podcast_id", "is_downloaded"]),
        Index(value = ["media_url"], unique = true)
    ]
)
data class EpisodeEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,
    @ColumnInfo(name = "podcast_id")
    val podcastId: String,
    @ColumnInfo(name = "guid")
    val guid: String? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "summary")
    val summary: String? = null,
    @ColumnInfo(name = "media_url")
    val mediaUrl: String,
    @ColumnInfo(name = "mime_type")
    val mimeType: String? = null,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long? = null,
    @ColumnInfo(name = "file_size_bytes")
    val fileSizeBytes: Long? = null,
    @ColumnInfo(name = "published_at")
    val publishedAt: Long? = null,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,
    @ColumnInfo(name = "season_number")
    val seasonNumber: Int? = null,
    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int? = null,
    @ColumnInfo(name = "explicit")
    val explicit: Boolean = false,
    @ColumnInfo(name = "is_played")
    val isPlayed: Boolean = false,
    @ColumnInfo(name = "played_at")
    val playedAt: Long? = null,
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,
    @ColumnInfo(name = "archived_at")
    val archivedAt: Long? = null,
    @ColumnInfo(name = "is_downloaded")
    val isDownloaded: Boolean = false,
    @ColumnInfo(name = "is_downloading")
    val isDownloading: Boolean = false,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "keep_download")
    val keepDownload: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
