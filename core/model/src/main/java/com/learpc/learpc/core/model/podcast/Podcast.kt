package com.learpc.learpc.core.model.podcast

data class Podcast(
    val id: String,
    val feedUrl: String,
    val title: String,
    val author: String? = null,
    val description: String? = null,
    val websiteUrl: String? = null,
    val artworkUrl: String? = null,
    val language: String? = null,
    val copyright: String? = null,
    val explicit: Boolean = false,
    val lastFeedPublishedAt: Long? = null,
    val lastRefreshedAt: Long? = null,
    val isActive: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
)
