package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.database.source.EpisodeLocalDataSource
import com.learpc.learpc.core.database.source.PodcastLocalDataSource
import com.learpc.learpc.core.network.rss.model.RemoteFeedEpisode
import com.learpc.learpc.core.network.rss.model.RemoteFeedPodcast
import com.learpc.learpc.core.network.rss.source.PodcastRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class RefreshPodcastFeedUseCaseTest {
    private val podcastRemoteDataSource = mockk<PodcastRemoteDataSource>()
    private val podcastLocalDataSource = mockk<PodcastLocalDataSource>(relaxed = true)
    private val episodeLocalDataSource = mockk<EpisodeLocalDataSource>(relaxed = true)

    private val useCase = RefreshPodcastFeedUseCase(
        podcastRemoteDataSource = podcastRemoteDataSource,
        podcastLocalDataSource = podcastLocalDataSource,
        episodeLocalDataSource = episodeLocalDataSource
    )

    @Test
    fun `deduplicates episodes with same guid`() = runTest {
        coEvery { podcastRemoteDataSource.fetchFeed(FEED_URL) } returns Result.success(
            RemoteFeedPodcast(
                title = "Sample Podcast",
                description = "Demo feed",
                artworkUrl = "https://example.com/artwork.jpg",
                episodes = listOf(
                    RemoteFeedEpisode(
                        guid = "episode-1",
                        title = "Episode One",
                        audioUrl = "https://example.com/audio/episode-1.mp3",
                        duration = "01:00:00",
                        pubDate = "Tue, 02 Jan 2024 10:00:00 GMT"
                    ),
                    RemoteFeedEpisode(
                        guid = "episode-1",
                        title = "Episode One Updated",
                        audioUrl = "https://example.com/audio/episode-1.mp3",
                        duration = "01:00:30",
                        pubDate = "Tue, 02 Jan 2024 10:00:00 GMT"
                    )
                )
            )
        )

        val result = useCase(PODCAST_ID, FEED_URL)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { podcastLocalDataSource.upsert(any()) }
        coVerify(exactly = 1) { episodeLocalDataSource.upsert(any()) }
    }

    private companion object {
        const val PODCAST_ID = "podcast-1"
        const val FEED_URL = "https://example.com/feed.xml"
    }
}
