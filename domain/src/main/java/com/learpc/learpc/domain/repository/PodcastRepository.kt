package com.learpc.learpc.domain.repository

import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.model.podcast.SubscriptionSettings
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    fun observePodcasts(): Flow<List<Podcast>>
    suspend fun getById(id: String): Podcast?
    suspend fun getSubscriptionSettings(podcastId: String): SubscriptionSettings?
    suspend fun upsert(podcast: Podcast)
    suspend fun delete(podcast: Podcast)
}
