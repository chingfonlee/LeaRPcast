package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.database.source.EpisodeLocalDataSource
import com.learpc.learpc.core.database.source.PodcastLocalDataSource
import com.learpc.learpc.core.network.rss.model.RemoteFeedEpisode
import com.learpc.learpc.core.network.rss.model.RemoteFeedPodcast
import com.learpc.learpc.core.network.rss.source.PodcastRemoteDataSource
import com.learpc.learpc.core.testing.IntegrationTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RefreshPodcastFeedUseCaseIntegrationTest : IntegrationTest() {
    private val podcastRemoteDataSource = mockk<PodcastRemoteDataSource>()

    private lateinit var useCase: RefreshPodcastFeedUseCase

    @Before
    fun setUp() {
        useCase = RefreshPodcastFeedUseCase(
            podcastRemoteDataSource = podcastRemoteDataSource,
            podcastLocalDataSource = PodcastLocalDataSource(database.podcastDao()),
            episodeLocalDataSource = EpisodeLocalDataSource(
                episodeDao = database.episodeDao(),
                playbackProgressDao = database.playbackProgressDao()
            )
        )
    }

    @Test
    fun `inserts three episodes and stays deduplicated on repeat refresh`() = runTest {
        coEvery { podcastRemoteDataSource.fetchFeed(FEED_URL) } returns Result.success(sampleFeed())

        val firstRun = useCase(PODCAST_ID, FEED_URL)
        val secondRun = useCase(PODCAST_ID, FEED_URL)

        assertTrue(firstRun.isSuccess)
        assertTrue(secondRun.isSuccess)
        assertEquals(3, database.episodeDao().observeAll().first().size)
        assertEquals("Sample Podcast", database.podcastDao().getById(PODCAST_ID)?.title)
        coVerify(exactly = 2) { podcastRemoteDataSource.fetchFeed(FEED_URL) }
    }

    private fun sampleFeed(): RemoteFeedPodcast {
        return RemoteFeedPodcast(
            title = "Sample Podcast",
            description = "Sample description",
            artworkUrl = "https://example.com/artwork.jpg",
            episodes = listOf(
                sampleEpisode(1),
                sampleEpisode(2),
                sampleEpisode(3)
            )
        )
    }

    private fun sampleEpisode(index: Int): RemoteFeedEpisode {
        val suffix = index.toString()
        return RemoteFeedEpisode(
            guid = "episode-$suffix",
            title = "Episode $suffix",
            audioUrl = "https://example.com/audio/episode-$suffix.mp3",
            duration = "00:10:0$suffix",
            pubDate = String.format(
                Locale.US,
                "Tue, 0%d Jan 2024 10:00:00 GMT",
                index
            )
        )
    }

    private companion object {
        const val PODCAST_ID = "podcast-1"
        const val FEED_URL = "https://example.com/feed.xml"
    }
}
