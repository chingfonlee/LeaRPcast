package com.learpc.learpc.feature.radio.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.feature.radio.R
import com.learpc.learpc.feature.radio.ui.viewmodel.RadioViewModel

private val RadioBackdrop = Brush.verticalGradient(
    colors = listOf(Color(0xFF08121E), Color(0xFF101B2B), Color(0xFF20131F))
)

@Composable
fun RadioScreen(
    viewModel: RadioViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    RadioContent(
        uiState = uiState,
        onPlayStation = viewModel::playStation,
        modifier = modifier
    )
}

@Composable
private fun RadioContent(
    uiState: com.learpc.learpc.feature.radio.ui.model.RadioUiState,
    onPlayStation: (RadioStation) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RadioBackdrop)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_library_label),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFE0B45C),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.radio_list_title),
                style = MaterialTheme.typography.displaySmall,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.radio_list_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD0C4B1)
            )

            Spacer(modifier = Modifier.height(22.dp))

            when {
                uiState.isLoading -> LoadingPanel()
                uiState.errorMessage != null -> ErrorPanel(message = uiState.errorMessage)
                uiState.stations.isEmpty() -> EmptyPanel()
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(uiState.stations, key = { it.id }) { station ->
                        RadioStationRow(
                            station = station,
                            onPlay = { onPlayStation(station) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingPanel() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x33E0B45C))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color(0xFFE0B45C)
            )
            Text(
                text = stringResource(R.string.radio_loading),
                color = Color(0xFFF6F1E8)
            )
        }
    }
}

@Composable
private fun ErrorPanel(message: String) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF311B1D),
        border = BorderStroke(1.dp, Color(0x66D59A92))
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(18.dp),
            color = Color(0xFFF4C8BF)
        )
    }
}

@Composable
private fun EmptyPanel() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.radio_empty_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.radio_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD0C4B1)
            )
        }
    }
}

@Composable
private fun RadioStationRow(
    station: RadioStation,
    onPlay: () -> Unit
) {
    val initials = station.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "R" }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE0B45C), Color(0xFFAA6A3E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0B1018),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = station.country ?: station.language ?: stringResource(R.string.radio_country_unknown),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD0C4B1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onPlay) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.radio_play_button),
                    tint = Color(0xFFE0B45C)
                )
            }
        }
    }
}
