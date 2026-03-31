package com.learpc.learpc.feature.downloads.ui.model

import com.learpc.learpc.core.model.download.DownloadRecord

data class DownloadsUiState(
    val isLoading: Boolean = true,
    val downloads: List<DownloadRecord> = emptyList(),
    val errorMessage: String? = null
)
