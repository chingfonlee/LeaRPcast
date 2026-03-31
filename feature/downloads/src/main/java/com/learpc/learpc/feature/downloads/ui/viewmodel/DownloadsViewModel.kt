package com.learpc.learpc.feature.downloads.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.offline.DownloadManager
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.domain.repository.DownloadRepository
import com.learpc.learpc.feature.downloads.ui.model.DownloadsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository,
    private val downloadManager: DownloadManager
) : ViewModel() {
    val downloadsState: StateFlow<DownloadsUiState> = downloadRepository.observeDownloads()
        .map { downloads ->
            DownloadsUiState(
                isLoading = false,
                downloads = downloads
            )
        }
        .onStart { emit(DownloadsUiState(isLoading = true)) }
        .catch { throwable ->
            emit(
                DownloadsUiState(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Could not load downloads."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DownloadsUiState()
        )

    fun cancelDownload(contentId: String) {
        downloadManager.removeDownload(contentId)
    }

    fun canCancel(record: DownloadRecord): Boolean {
        return record.status == DownloadStatus.QUEUED || record.status == DownloadStatus.DOWNLOADING
    }
}
