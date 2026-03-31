package com.learpc.learpc.core.network.rss.source

import com.learpc.learpc.core.network.rss.FeedParser
import com.learpc.learpc.core.network.rss.PodcastFeedService
import com.learpc.learpc.core.network.rss.model.RemoteFeedPodcast
import javax.inject.Inject

class PodcastRemoteDataSource @Inject constructor(
    private val podcastFeedService: PodcastFeedService,
    private val feedParser: FeedParser
) {
    suspend fun fetchFeed(url: String): Result<RemoteFeedPodcast> {
        return podcastFeedService.fetchFeed(url).fold(
            onSuccess = { xml -> feedParser.parse(xml) },
            onFailure = { Result.failure(it) }
        )
    }
}
