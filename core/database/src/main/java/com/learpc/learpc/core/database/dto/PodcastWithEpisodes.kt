package com.learpc.learpc.core.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.learpc.learpc.core.database.entities.EpisodeEntity
import com.learpc.learpc.core.database.entities.PodcastEntity

data class PodcastWithEpisodes(
    @Embedded val podcast: PodcastEntity,
    @Relation(
        parentColumn = "podcast_id",
        entityColumn = "podcast_id"
    )
    val episodes: List<EpisodeEntity>
)
