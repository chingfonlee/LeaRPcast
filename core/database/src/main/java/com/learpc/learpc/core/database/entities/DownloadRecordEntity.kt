package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "download_record",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["episode_id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["episode_id"], unique = true),
        Index(value = ["status"]),
        Index(value = ["downloaded_at"]),
        Index(value = ["local_file_uri"], unique = true)
    ]
)
data class DownloadRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,
    @ColumnInfo(name = "download_request_id")
    val downloadRequestId: String? = null,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "failure_reason")
    val failureReason: String? = null,
    @ColumnInfo(name = "bytes_downloaded")
    val bytesDownloaded: Long = 0,
    @ColumnInfo(name = "total_bytes")
    val totalBytes: Long? = null,
    @ColumnInfo(name = "local_file_uri")
    val localFileUri: String? = null,
    @ColumnInfo(name = "local_file_size_bytes")
    val localFileSizeBytes: Long? = null,
    @ColumnInfo(name = "download_started_at")
    val downloadStartedAt: Long? = null,
    @ColumnInfo(name = "downloaded_at")
    val downloadedAt: Long? = null,
    @ColumnInfo(name = "last_error_at")
    val lastErrorAt: Long? = null,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
