package com.learpc.learpc.feature.downloads.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.core.model.download.DownloadStatus
import com.learpc.learpc.feature.downloads.R
import com.learpc.learpc.feature.downloads.ui.viewmodel.DownloadsViewModel

@Composable
fun DownloadsScreen(
    viewModel: DownloadsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val downloadsState by viewModel.downloadsState.collectAsState()

    DownloadsContent(
        downloads = downloadsState.downloads,
        isLoading = downloadsState.isLoading,
        errorMessage = downloadsState.errorMessage,
        onCancel = viewModel::cancelDownload,
        canCancel = viewModel::canCancel,
        modifier = modifier
    )
}

@Composable
private fun DownloadsContent(
    downloads: List<DownloadRecord>,
    isLoading: Boolean,
    errorMessage: String?,
    onCancel: (String) -> Unit,
    canCancel: (DownloadRecord) -> Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        when {
            isLoading -> Text(text = stringResource(R.string.downloads_loading))
            errorMessage != null -> Text(text = errorMessage)
            downloads.isEmpty() -> EmptyDownloadsCard()
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(downloads, key = { it.episodeId }) { record ->
                    DownloadRow(
                        record = record,
                        canCancel = canCancel(record),
                        onCancel = { onCancel(record.episodeId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadRow(
    record: DownloadRecord,
    canCancel: Boolean,
    onCancel: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.episodeId,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1C1816),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusLabel(record.status),
                    color = Color(0xFF6E6258)
                )
            }
            if (canCancel) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.download_cancel_button)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyDownloadsCard() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.downloads_empty),
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun statusLabel(status: DownloadStatus): String {
    return when (status) {
        DownloadStatus.QUEUED -> stringResource(R.string.download_status_queued)
        DownloadStatus.DOWNLOADING -> stringResource(R.string.download_status_downloading)
        DownloadStatus.COMPLETED -> stringResource(R.string.download_status_completed)
        DownloadStatus.FAILED -> stringResource(R.string.download_status_failed)
        DownloadStatus.REMOVING -> stringResource(R.string.download_status_removing)
    }
}
