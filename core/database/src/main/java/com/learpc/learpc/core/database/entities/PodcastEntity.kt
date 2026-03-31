package com.learpc.learpc.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "podcast",
    indices = [
        Index(value = ["title"]),
        Index(value = ["author"]),
        Index(value = ["feed_url"], unique = true),
        Index(value = ["is_active"])
    ]
)
data class PodcastEntity(
    @PrimaryKey
    @ColumnInfo(name = "podcast_id")
    val podcastId: String,
    @ColumnInfo(name = "feed_url")
    val feedUrl: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "website_url")
    val websiteUrl: String? = null,
    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,
    @ColumnInfo(name = "language")
    val language: String? = null,
    @ColumnInfo(name = "copyright")
    val copyright: String? = null,
    @ColumnInfo(name = "explicit")
    val explicit: Boolean = false,
    @ColumnInfo(name = "last_feed_published_at")
    val lastFeedPublishedAt: Long? = null,
    @ColumnInfo(name = "last_refreshed_at")
    val lastRefreshedAt: Long? = null,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
