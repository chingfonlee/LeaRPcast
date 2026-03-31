package com.learpc.learpc.domain.usecase

import androidx.media3.exoplayer.offline.DownloadManager
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.DownloadRepository
import javax.inject.Inject

class EnqueueEpisodeDownloadUseCase @Inject constructor(
    private val downloadManager: DownloadManager,
    private val downloadRepository: DownloadRepository,
    private val downloadRequestFactory: DownloadRequestFactory
) {
    suspend operator fun invoke(episode: Episode) {
        val existing = downloadRepository.getByEpisodeId(episode.id)
        if (existing != null && existing.status != DownloadStatus.FAILED) {
            return
        }

        val request = downloadRequestFactory.create(episode)
        downloadManager.addDownload(request)
        downloadRepository.upsertRecord(
            DownloadRecord(
                episodeId = episode.id,
                downloadRequestId = episode.id,
                status = DownloadStatus.QUEUED,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
