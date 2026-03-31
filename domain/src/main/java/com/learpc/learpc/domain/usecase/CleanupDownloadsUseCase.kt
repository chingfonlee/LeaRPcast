package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.DownloadRepository
import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.domain.repository.SettingsRepository
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CleanupDownloadsUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository,
    private val downloadRepository: DownloadRepository,
    private val settingsRepository: SettingsRepository,
    private val playbackController: PlaybackController
) {
    suspend operator fun invoke(nowEpochMs: Long = System.currentTimeMillis()): Int = withContext(Dispatchers.IO) {
        val settings = settingsRepository.observeSettings().first()
        if (settings.autoDeleteMode == AutoDeleteMode.NEVER) return@withContext 0

        val currentItemId = playbackController.currentItem.value?.id
        val episodesById = episodeRepository.observeAll().first().associateBy(Episode::id)
        val completedDownloads = downloadRepository.observeDownloads().first()
            .filter { it.status == DownloadStatus.COMPLETED }

        var deletedCount = 0
        for (record in completedDownloads) {
            val episode = episodesById[record.episodeId] ?: continue
            if (!shouldDelete(episode, record, settings, currentItemId, nowEpochMs)) continue

            deleteFile(record.localFileUri ?: episode.localFileUri)
            episodeRepository.clearDownloadedState(episode.id)
            downloadRepository.delete(record)
            deletedCount += 1
        }

        deletedCount
    }

    private fun shouldDelete(
        episode: Episode,
        record: DownloadRecord,
        settings: UserPreferences,
        currentItemId: String?,
        nowEpochMs: Long
    ): Boolean {
        if (episode.id == currentItemId) return false
        if (!episode.isDownloaded) return false
        if (episode.keepDownload) return false

        val downloadedAt = record.downloadedAt ?: return false
        val ageMs = nowEpochMs - downloadedAt
        if (ageMs < 0) return false

        val retentionMs = when (settings.autoDeleteMode) {
            AutoDeleteMode.NEVER -> return false
            AutoDeleteMode.AFTER_PLAYED -> settings.autoDeleteAfterHours.coerceAtLeast(0) * HOUR_MS
            AutoDeleteMode.AFTER_24_HOURS -> 24L * HOUR_MS
            AutoDeleteMode.AFTER_7_DAYS -> 7L * DAY_MS
        }

        return ageMs >= retentionMs
    }

    private fun deleteFile(localFileUri: String?) {
        val path = localFileUri?.toFilePath() ?: return
        runCatching { File(path).delete() }
    }

    private fun String.toFilePath(): String {
        return if (startsWith("file://")) {
            java.net.URI(this).path ?: this
        } else {
            this
        }
    }

    private companion object {
        const val HOUR_MS = 60 * 60 * 1000L
        const val DAY_MS = 24 * HOUR_MS
    }
}
