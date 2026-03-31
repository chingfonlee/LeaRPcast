package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.source.PodcastLocalDataSource
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.model.podcast.SubscriptionSettings
import com.learpc.learpc.domain.repository.PodcastRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPodcastRepository @Inject constructor(
    private val podcastLocalDataSource: PodcastLocalDataSource
) : PodcastRepository {
    override fun observePodcasts(): Flow<List<Podcast>> {
        return podcastLocalDataSource.observeAll()
    }

    override suspend fun getById(id: String): Podcast? {
        return podcastLocalDataSource.getById(id)
    }

    override suspend fun getSubscriptionSettings(podcastId: String): SubscriptionSettings? {
        return podcastLocalDataSource.getSubscriptionSettings(podcastId)
    }

    override suspend fun upsert(podcast: Podcast) {
        podcastLocalDataSource.upsert(podcast)
    }

    override suspend fun delete(podcast: Podcast) {
        podcastLocalDataSource.delete(podcast)
    }
}
