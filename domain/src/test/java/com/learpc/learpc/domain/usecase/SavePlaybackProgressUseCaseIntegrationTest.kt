package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.database.source.PodcastLocalDataSource
import com.learpc.learpc.core.database.source.EpisodeLocalDataSource
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.core.testing.IntegrationTest
import com.learpc.learpc.domain.repository.EpisodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)

class SavePlaybackProgressUseCaseIntegrationTest : IntegrationTest() {
    private lateinit var podcastLocalDataSource: PodcastLocalDataSource
    private lateinit var episodeLocalDataSource: EpisodeLocalDataSource
    private lateinit var useCase: SavePlaybackProgressUseCase

    @Before
    fun setUp() {
        podcastLocalDataSource = PodcastLocalDataSource(database.podcastDao())
        episodeLocalDataSource = EpisodeLocalDataSource(
            episodeDao = database.episodeDao(),
            playbackProgressDao = database.playbackProgressDao()
        )
        useCase = SavePlaybackProgressUseCase(
            episodeRepository = TestEpisodeRepository(episodeLocalDataSource)
        )
    }

    @Test
    fun `persists playback position in playback progress table`() = runTest {
        podcastLocalDataSource.upsert(samplePodcast())
        episodeLocalDataSource.upsert(sampleEpisode())

        useCase(
            episodeId = EPISODE_ID,
            positionMs = 45_000L,
            isCompleted = false
        )

        val saved = database.playbackProgressDao().getByEpisodeId(EPISODE_ID)

        assertNotNull(saved)
        assertEquals(45_000L, saved?.positionMs)
        assertEquals("IN_PROGRESS", saved?.completionState)
    }

    private class TestEpisodeRepository(
        private val episodeLocalDataSource: EpisodeLocalDataSource
    ) : EpisodeRepository {
        override fun observeByPodcastId(podcastId: String): Flow<List<Episode>> {
            error("Not used in this test")
        }

        override fun observeAll(): Flow<List<Episode>> {
            error("Not used in this test")
        }

        override suspend fun getById(id: String): Episode? {
            return episodeLocalDataSource.getById(id)
        }

        override suspend fun upsert(episode: Episode) {
            episodeLocalDataSource.upsert(episode)
        }

        override suspend fun updateProgress(
            episodeId: String,
            positionMs: Long,
            isCompleted: Boolean
        ) {
            episodeLocalDataSource.updateProgress(episodeId, positionMs, isCompleted)
        }

        override suspend fun clearDownloadedState(episodeId: String) {
            error("Not used in this test")
        }

        override suspend fun delete(episode: Episode) {
            error("Not used in this test")
        }
    }

    private fun sampleEpisode() = Episode(
        id = EPISODE_ID,
        podcastId = PODCAST_ID,
        title = "Episode 1",
        mediaUrl = "https://example.com/episode-1.mp3",
        durationMs = 60_000L
    )

    private fun samplePodcast() = Podcast(
        id = PODCAST_ID,
        feedUrl = "https://example.com/feed.xml",
        title = "Sample Podcast",
        createdAt = NOW,
        updatedAt = NOW
    )

    private companion object {
        const val PODCAST_ID = "podcast-1"
        const val EPISODE_ID = "episode-1"
        const val NOW = 1_700_000_000_000L
    }
}
