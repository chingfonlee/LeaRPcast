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
        Index(value = ["country_code"]),
        Index(value = ["language"]),
        Index(value = ["genre"]),
        Index(value = ["ui_primary_group"]),
        Index(value = ["network"]),
        Index(value = ["region"]),
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
    @ColumnInfo(name = "display_name")
    val displayName: String? = null,
    @ColumnInfo(name = "display_frequency")
    val displayFrequency: String? = null,
    @ColumnInfo(name = "frequency")
    val frequency: String? = null,
    @ColumnInfo(name = "band")
    val band: String? = null,
    @ColumnInfo(name = "source_group")
    val sourceGroup: String? = null,
    @ColumnInfo(name = "network")
    val network: String? = null,
    @ColumnInfo(name = "region")
    val region: String? = null,
    @ColumnInfo(name = "category")
    val category: String? = null,
    @ColumnInfo(name = "media_type")
    val mediaType: String? = null,
    @ColumnInfo(name = "ui_primary_group")
    val uiPrimaryGroup: String? = null,
    @ColumnInfo(name = "ui_secondary_group")
    val uiSecondaryGroup: String? = null,
    @ColumnInfo(name = "country")
    val country: String? = null,
    @ColumnInfo(name = "country_code")
    val countryCode: String? = null,
    @ColumnInfo(name = "language")
    val language: String? = null,
    @ColumnInfo(name = "genre")
    val genre: String? = null,
    @ColumnInfo(name = "codec")
    val codec: String? = null,
    @ColumnInfo(name = "bitrate_kbps")
    val bitrateKbps: Int? = null,
    @ColumnInfo(name = "search_keywords_json")
    val searchKeywordsJson: String? = null,
    @ColumnInfo(name = "aliases_json")
    val aliasesJson: String? = null,
    @ColumnInfo(name = "merged_from_ids_json")
    val mergedFromIdsJson: String? = null,
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
