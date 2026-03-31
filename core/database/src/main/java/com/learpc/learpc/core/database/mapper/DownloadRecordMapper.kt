package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.DownloadRecordEntity
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus

object DownloadRecordMapper {
    fun DownloadRecordEntity.toDomain(): DownloadRecord {
        return DownloadRecord(
            episodeId = episodeId,
            downloadRequestId = downloadRequestId,
            status = status.toDownloadStatus(),
            failureReason = failureReason,
            bytesDownloaded = bytesDownloaded,
            totalBytes = totalBytes,
            localFileUri = localFileUri,
            localFileSizeBytes = localFileSizeBytes,
            downloadStartedAt = downloadStartedAt,
            downloadedAt = downloadedAt,
            lastErrorAt = lastErrorAt,
            updatedAt = updatedAt
        )
    }

    fun DownloadRecord.toEntity(): DownloadRecordEntity {
        return DownloadRecordEntity(
            episodeId = episodeId,
            downloadRequestId = downloadRequestId,
            status = status.name,
            failureReason = failureReason,
            bytesDownloaded = bytesDownloaded,
            totalBytes = totalBytes,
            localFileUri = localFileUri,
            localFileSizeBytes = localFileSizeBytes,
            downloadStartedAt = downloadStartedAt,
            downloadedAt = downloadedAt,
            lastErrorAt = lastErrorAt,
            updatedAt = updatedAt
        )
    }

    private fun String.toDownloadStatus(): DownloadStatus {
        return runCatching { DownloadStatus.valueOf(this) }.getOrDefault(DownloadStatus.FAILED)
    }
}
