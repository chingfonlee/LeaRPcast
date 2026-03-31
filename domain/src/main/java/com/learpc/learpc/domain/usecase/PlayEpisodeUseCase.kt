package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.mapper.EpisodePlayableMapper
import java.io.File
import javax.inject.Inject

class PlayEpisodeUseCase @Inject constructor(
    private val playbackController: PlaybackController,
    private val episodePlayableMapper: EpisodePlayableMapper
) {
    operator fun invoke(episode: Episode) {
        val mediaUri = episode.localFileUri
            ?.takeIf { episode.isDownloaded && File(it).exists() }
            ?: episode.mediaUrl

        playbackController.setItem(episodePlayableMapper.toPlayableItem(episode, mediaUri))
        playbackController.play()
    }
}
