package com.learpc.learpc.core.database.source

import com.learpc.learpc.core.database.dao.PodcastDao
import com.learpc.learpc.core.database.mapper.SubscriptionSettingsMapper.toDomain
import com.learpc.learpc.core.database.mapper.PodcastMapper.toDomain
import com.learpc.learpc.core.database.mapper.PodcastMapper.toEntity
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.model.podcast.SubscriptionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
) {
    fun observeAll(): Flow<List<Podcast>> {
        return podcastDao.observeAll().map { podcasts ->
            podcasts.map { it.toDomain() }
        }
    }

    suspend fun getById(id: String): Podcast? {
        return podcastDao.getById(id)?.toDomain()
    }

    suspend fun getSubscriptionSettings(podcastId: String): SubscriptionSettings? {
        return podcastDao.getSubscriptionSettings(podcastId)?.subscriptionSettings?.toDomain()
    }

    suspend fun upsert(podcast: Podcast) {
        podcastDao.upsert(podcast.toEntity())
    }

    suspend fun delete(podcast: Podcast) {
        podcastDao.delete(podcast.toEntity())
    }
}
