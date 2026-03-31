package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.mapper.EpisodePlayableMapper
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import io.mockk.verify
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlayEpisodeUseCaseTest {
    @MockK
    private lateinit var playbackController: PlaybackController

    private lateinit var useCase: PlayEpisodeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = PlayEpisodeUseCase(
            playbackController = playbackController,
            episodePlayableMapper = EpisodePlayableMapper()
        )
        every { playbackController.setItem(any()) } just Runs
        every { playbackController.play() } just Runs
    }

    @Test
    fun `uses local file when episode is downloaded`() {
        val localFile = File.createTempFile("episode", ".mp3")
        localFile.deleteOnExit()
        val episode = Episode(
            id = "episode-1",
            podcastId = "podcast-1",
            title = "Downloaded Episode",
            mediaUrl = "https://example.com/episode-1.mp3",
            localFileUri = localFile.absolutePath,
            isDownloaded = true
        )
        val itemSlot = slot<com.learpc.learpc.core.model.PlayableItem>()

        every { playbackController.setItem(capture(itemSlot)) } just Runs

        useCase(episode)

        assertEquals(localFile.absolutePath, itemSlot.captured.mediaUri)
        verify(exactly = 1) { playbackController.play() }
    }

    @Test
    fun `uses remote url when episode is not downloaded`() {
        val episode = Episode(
            id = "episode-2",
            podcastId = "podcast-1",
            title = "Streaming Episode",
            mediaUrl = "https://example.com/episode-2.mp3",
            localFileUri = null,
            isDownloaded = false
        )
        val itemSlot = slot<com.learpc.learpc.core.model.PlayableItem>()

        every { playbackController.setItem(capture(itemSlot)) } just Runs

        useCase(episode)

        assertEquals(episode.mediaUrl, itemSlot.captured.mediaUri)
        verify(exactly = 1) { playbackController.play() }
    }
}
