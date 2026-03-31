package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playback_progress",
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
        Index(value = ["last_played_at"]),
        Index(value = ["completion_state"])
    ]
)
data class PlaybackProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,
    @ColumnInfo(name = "position_ms")
    val positionMs: Long,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long? = null,
    @ColumnInfo(name = "completion_state")
    val completionState: String,
    @ColumnInfo(name = "play_count")
    val playCount: Int = 0,
    @ColumnInfo(name = "last_played_at")
    val lastPlayedAt: Long? = null,
    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
