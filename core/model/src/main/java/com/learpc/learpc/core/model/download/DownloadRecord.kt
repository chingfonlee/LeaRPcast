package com.learpc.learpc.core.model.download

data class DownloadRecord(
    val episodeId: String,
    val downloadRequestId: String? = null,
    val status: DownloadStatus,
    val failureReason: String? = null,
    val bytesDownloaded: Long = 0,
    val totalBytes: Long? = null,
    val localFileUri: String? = null,
    val localFileSizeBytes: Long? = null,
    val downloadStartedAt: Long? = null,
    val downloadedAt: Long? = null,
    val lastErrorAt: Long? = null,
    val updatedAt: Long
)
