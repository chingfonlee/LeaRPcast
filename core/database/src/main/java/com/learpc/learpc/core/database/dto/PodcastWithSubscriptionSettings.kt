package com.learpc.learpc.core.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.learpc.learpc.core.database.entities.PodcastEntity
import com.learpc.learpc.core.database.entities.SubscriptionSettingsEntity

data class PodcastWithSubscriptionSettings(
    @Embedded val podcast: PodcastEntity,
    @Relation(
        parentColumn = "podcast_id",
        entityColumn = "podcast_id"
    )
    val subscriptionSettings: SubscriptionSettingsEntity?
)
