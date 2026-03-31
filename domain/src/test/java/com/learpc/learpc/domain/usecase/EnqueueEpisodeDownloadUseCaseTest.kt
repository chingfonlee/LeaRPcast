package com.learpc.learpc.domain.usecase

import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.DownloadRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class EnqueueEpisodeDownloadUseCaseTest {
    private val downloadManager = mockk<DownloadManager>(relaxed = true)
    private val downloadRepository = mockk<DownloadRepository>(relaxed = true)
    private val downloadRequestFactory = mockk<DownloadRequestFactory>()
    private val downloadRequest = mockk<DownloadRequest>(relaxed = true)
    private val useCase = EnqueueEpisodeDownloadUseCase(
        downloadManager,
        downloadRepository,
        downloadRequestFactory
    )

    @Test
    fun `returns early when existing non failed record exists`() = runTest {
        coEvery { downloadRepository.getByEpisodeId("episode-1") } returns DownloadRecord(
            episodeId = "episode-1",
            status = DownloadStatus.QUEUED,
            updatedAt = 0L
        )

        useCase(sampleEpisode())

        coVerify(exactly = 0) { downloadManager.addDownload(any()) }
    }

    @Test
    fun `queues download when no record exists`() = runTest {
        coEvery { downloadRepository.getByEpisodeId("episode-1") } returns null
        coEvery { downloadRepository.upsertRecord(any()) } returns Unit
        coEvery { downloadRequestFactory.create(any()) } returns downloadRequest

        useCase(sampleEpisode())

        coVerify(exactly = 1) { downloadManager.addDownload(downloadRequest) }
        coVerify(exactly = 1) {
            downloadRepository.upsertRecord(match { it.status == DownloadStatus.QUEUED })
        }
    }

    private fun sampleEpisode() = Episode(
        id = "episode-1",
        podcastId = "podcast-1",
        title = "Episode 1",
        mediaUrl = "https://example.com/episode-1.mp3",
        isDownloaded = false
    )
}
