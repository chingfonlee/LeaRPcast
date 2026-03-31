package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.DownloadRepository
import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CleanupDownloadsUseCaseTest {
    private val episodeRepository = mockk<EpisodeRepository>()
    private val downloadRepository = mockk<DownloadRepository>()
    private val settingsRepository = mockk<SettingsRepository>()
    private val playbackController = mockk<PlaybackController>()

    private lateinit var useCase: CleanupDownloadsUseCase

    @Before
    fun setUp() {
        useCase = CleanupDownloadsUseCase(
            episodeRepository = episodeRepository,
            downloadRepository = downloadRepository,
            settingsRepository = settingsRepository,
            playbackController = playbackController
        )
    }

    @Test
    fun `currently playing episode is protected from deletion`() = runTest {
        val episode = sampleEpisode(localFileUri = "file:///tmp/episode.mp3")
        val record = sampleRecord(downloadedAt = now - 48 * HOUR_MS)

        every { playbackController.currentItem } returns MutableStateFlow(samplePlayableItem(episode.id))
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())
        every { episodeRepository.observeAll() } returns flowOf(listOf(episode))
        every { downloadRepository.observeDownloads() } returns flowOf(listOf(record))

        val deletedCount = useCase(now)

        assertEquals(0, deletedCount)
        coVerify(exactly = 0) { episodeRepository.clearDownloadedState(any()) }
        coVerify(exactly = 0) { downloadRepository.delete(any()) }
    }

    @Test
    fun `episode within retention period is not deleted`() = runTest {
        val episode = sampleEpisode()
        val record = sampleRecord(downloadedAt = now - 2 * HOUR_MS)

        every { playbackController.currentItem } returns MutableStateFlow(null)
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences(autoDeleteMode = AutoDeleteMode.AFTER_24_HOURS))
        every { episodeRepository.observeAll() } returns flowOf(listOf(episode))
        every { downloadRepository.observeDownloads() } returns flowOf(listOf(record))

        val deletedCount = useCase(now)

        assertEquals(0, deletedCount)
        coVerify(exactly = 0) { episodeRepository.clearDownloadedState(any()) }
        coVerify(exactly = 0) { downloadRepository.delete(any()) }
    }

    @Test
    fun `keep download episode is not deleted`() = runTest {
        val episode = sampleEpisode(keepDownload = true)
        val record = sampleRecord(downloadedAt = now - 48 * HOUR_MS)

        every { playbackController.currentItem } returns MutableStateFlow(null)
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())
        every { episodeRepository.observeAll() } returns flowOf(listOf(episode))
        every { downloadRepository.observeDownloads() } returns flowOf(listOf(record))

        val deletedCount = useCase(now)

        assertEquals(0, deletedCount)
        coVerify(exactly = 0) { episodeRepository.clearDownloadedState(any()) }
        coVerify(exactly = 0) { downloadRepository.delete(any()) }
    }

    @Test
    fun `deletes eligible episode and clears db state`() = runTest {
        val episode = sampleEpisode()
        val record = sampleRecord(downloadedAt = now - 48 * HOUR_MS)

        every { playbackController.currentItem } returns MutableStateFlow(null)
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())
        every { episodeRepository.observeAll() } returns flowOf(listOf(episode))
        every { downloadRepository.observeDownloads() } returns flowOf(listOf(record))

        coEvery { episodeRepository.clearDownloadedState(episode.id) } returns Unit
        coEvery { downloadRepository.delete(record) } returns Unit

        val deletedCount = useCase(now)

        assertEquals(1, deletedCount)
        coVerify(exactly = 1) { episodeRepository.clearDownloadedState(episode.id) }
        coVerify(exactly = 1) { downloadRepository.delete(record) }
    }

    private val now = 1_700_000_000_000L

    private fun sampleEpisode(
        keepDownload: Boolean = false,
        isPlayed: Boolean = true,
        isDownloaded: Boolean = true,
        localFileUri: String? = "file:///tmp/episode.mp3"
    ) = Episode(
        id = "episode-1",
        podcastId = "podcast-1",
        title = "Episode 1",
        mediaUrl = "https://example.com/episode-1.mp3",
        localFileUri = localFileUri,
        publishedAt = now - 48 * HOUR_MS,
        isPlayed = isPlayed,
        isDownloaded = isDownloaded,
        keepDownload = keepDownload
    )

    private fun sampleRecord(downloadedAt: Long) = DownloadRecord(
        episodeId = "episode-1",
        status = DownloadStatus.COMPLETED,
        downloadedAt = downloadedAt,
        updatedAt = downloadedAt
    )

    private fun samplePreferences(
        autoDeleteMode: AutoDeleteMode = AutoDeleteMode.AFTER_24_HOURS,
        autoDeleteAfterHours: Int = 24
    ) = UserPreferences(
        autoDeleteMode = autoDeleteMode,
        autoDeleteAfterHours = autoDeleteAfterHours
    )

    private fun samplePlayableItem(id: String) = PlayableItem(
        id = id,
        title = "Playing",
        mediaUri = "https://example.com/playing.mp3"
    )

    private companion object {
        const val HOUR_MS = 60 * 60 * 1000L
    }
}
