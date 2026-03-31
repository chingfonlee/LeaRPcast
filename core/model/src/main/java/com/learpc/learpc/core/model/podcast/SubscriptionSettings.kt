package com.learpc.learpc.core.model.podcast

data class SubscriptionSettings(
    val podcastId: String,
    val autoDownloadEnabled: Boolean,
    val wifiOnlyOverride: Boolean? = null,
    val downloadLimitOverride: Int? = null
)
