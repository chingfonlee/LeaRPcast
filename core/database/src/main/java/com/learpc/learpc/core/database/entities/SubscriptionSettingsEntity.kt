package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subscription",
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
        Index(value = ["podcast_id"], unique = true),
        Index(value = ["is_subscribed"]),
        Index(value = ["auto_download_enabled"])
    ]
)
data class SubscriptionSettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = "podcast_id")
    val podcastId: String,
    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean = true,
    @ColumnInfo(name = "subscribed_at")
    val subscribedAt: Long,
    @ColumnInfo(name = "auto_download_enabled")
    val autoDownloadEnabled: Boolean = true,
    @ColumnInfo(name = "wifi_only_override")
    val wifiOnlyOverride: Boolean? = null,
    @ColumnInfo(name = "download_limit_override")
    val downloadLimitOverride: Int? = null,
    @ColumnInfo(name = "auto_delete_mode_override")
    val autoDeleteModeOverride: String? = null,
    @ColumnInfo(name = "playback_speed_override")
    val playbackSpeedOverride: Float? = null,
    @ColumnInfo(name = "skip_intro_seconds")
    val skipIntroSeconds: Int? = null,
    @ColumnInfo(name = "skip_outro_seconds")
    val skipOutroSeconds: Int? = null,
    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = false,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
