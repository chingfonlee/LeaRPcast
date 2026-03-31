package com.learpc.learpc.domain.mapper

import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.podcast.Episode
import javax.inject.Inject

class EpisodePlayableMapper @Inject constructor() {
    fun toPlayableItem(episode: Episode, mediaUri: String): PlayableItem {
        return PlayableItem(
            id = episode.id,
            title = episode.title,
            subtitle = null,
            imageUri = null,
            mediaUri = mediaUri
        )
    }
}
