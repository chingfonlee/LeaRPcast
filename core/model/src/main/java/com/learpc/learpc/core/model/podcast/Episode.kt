package com.learpc.learpc.core.model.podcast

import com.learpc.learpc.core.model.download.DownloadStatus

data class Episode(
    val id: String,
    val podcastId: String,
    val title: String,
    val mediaUrl: String,
    val localFileUri: String? = null,
    val publishedAt: Long? = null,
    val durationMs: Long? = null,
    val playbackPositionMs: Long = 0L,
    val isPlayed: Boolean = false,
    val isDownloaded: Boolean = false,
    val downloadStatus: DownloadStatus? = null,
    val keepDownload: Boolean = false
)
