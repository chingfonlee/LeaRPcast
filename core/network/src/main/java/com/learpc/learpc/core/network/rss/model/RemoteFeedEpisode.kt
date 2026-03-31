package com.learpc.learpc.core.network.rss.model

data class RemoteFeedEpisode(
    val guid: String,
    val title: String,
    val audioUrl: String,
    val duration: String? = null,
    val pubDate: String? = null
)
