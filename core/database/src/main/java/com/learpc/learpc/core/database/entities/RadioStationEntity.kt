package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "radio_station",
    indices = [
        Index(value = ["name"]),
        Index(value = ["country"]),
        Index(value = ["language"]),
        Index(value = ["genre"]),
        Index(value = ["is_favorite"])
    ]
)
data class RadioStationEntity(
    @PrimaryKey
    @ColumnInfo(name = "station_id")
    val stationId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "stream_url")
    val streamUrl: String,
    @ColumnInfo(name = "backup_stream_url")
    val backupStreamUrl: String? = null,
    @ColumnInfo(name = "homepage_url")
    val homepageUrl: String? = null,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,
    @ColumnInfo(name = "country")
    val country: String? = null,
    @ColumnInfo(name = "language")
    val language: String? = null,
    @ColumnInfo(name = "genre")
    val genre: String? = null,
    @ColumnInfo(name = "codec")
    val codec: String? = null,
    @ColumnInfo(name = "bitrate_kbps")
    val bitrateKbps: Int? = null,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int? = null,
    @ColumnInfo(name = "last_synced_at")
    val lastSyncedAt: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
