package com.learpc.learpc.core.network.rss.model

data class RemoteFeedPodcast(
    val title: String,
    val description: String? = null,
    val artworkUrl: String? = null,
    val episodes: List<RemoteFeedEpisode> = emptyList()
)
