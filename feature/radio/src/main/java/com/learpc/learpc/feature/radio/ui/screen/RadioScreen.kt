package com.learpc.learpc.feature.radio.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.learpc.learpc.core.model.PlaybackStateModel
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.feature.radio.R
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import com.learpc.learpc.feature.radio.ui.model.RadioQuickAddState
import com.learpc.learpc.feature.radio.ui.model.RadioStationEditorState
import com.learpc.learpc.feature.radio.ui.viewmodel.RadioViewModel
import java.util.Locale

private val RadioBackdrop = Brush.verticalGradient(
    colors = listOf(Color(0xFF08121E), Color(0xFF101B2B), Color(0xFF20131F))
)

@Composable
fun RadioScreen(
    viewModel: RadioViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier) {
        RadioShellScreen(
            uiState = uiState,
            onShellTabSelected = viewModel::selectShellTab,
            onPlayStation = viewModel::playStation,
            onToggleFavorite = viewModel::toggleFavorite,
            onAddStation = viewModel::showQuickAddStationEditor,
            onRetryLoad = viewModel::retryLoadStations,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onBrowseSortModeChange = viewModel::selectBrowseSortMode,
            onStationsTaxonomyModeChange = viewModel::selectStationsTaxonomyMode,
            onRetryPlayback = viewModel::retryPlayback,
            onTogglePlayback = viewModel::togglePlayback,
            modifier = Modifier
        )

        uiState.quickAddEditor?.let { editor ->
            QuickAddDialog(
                editor = editor,
                onEditorChange = viewModel::updateQuickAddEditor,
                onDismiss = viewModel::dismissQuickAddStationEditor,
                onSave = viewModel::saveQuickAddStation
            )
        }

        uiState.stationEditor?.let { editor ->
            RadioStationEditorDialog(
                editor = editor,
                onEditorChange = viewModel::updateStationEditor,
                onDismiss = viewModel::dismissStationEditor,
                onSave = viewModel::saveStation
            )
        }
    }
}

@Composable
fun RadioHomeContent(
    uiState: com.learpc.learpc.feature.radio.ui.model.RadioUiState,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onAddStation: () -> Unit,
    onRetryLoad: () -> Unit,
    onQuickAddChange: (RadioQuickAddState) -> Unit,
    onDismissQuickAdd: () -> Unit,
    onSaveQuickAdd: () -> Unit,
    onStationEditorChange: (RadioStationEditorState) -> Unit,
    onDismissStationEditor: () -> Unit,
    onSaveStation: () -> Unit,
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
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderBar(
                isLoading = uiState.isLoading,
                onAddStation = onAddStation
            )

            when (uiState.homeStatus) {
                RadioHomeStatus.Loading -> LoadingCard()
                RadioHomeStatus.NetworkFailure -> ErrorCard(
                    title = stringResource(R.string.radio_network_failure_title),
                    message = uiState.networkErrorMessage
                        ?: stringResource(R.string.radio_network_failure_body),
                    actionLabel = stringResource(R.string.radio_retry_button),
                    onAction = onRetryLoad
                )
                RadioHomeStatus.PlaybackFailure -> ErrorCard(
                    title = stringResource(R.string.radio_playback_failure_title),
                    message = uiState.playbackErrorMessage
                        ?: stringResource(R.string.radio_playback_failure_body),
                    actionLabel = stringResource(R.string.radio_retry_playback_button),
                    onAction = {
                        currentStation(uiState)?.let(onPlayStation)
                    }
                )
                RadioHomeStatus.AddSuccess -> SuccessCard(
                    title = stringResource(R.string.radio_add_success_title),
                    message = uiState.addSuccessMessage?.let {
                        stringResource(R.string.radio_add_success_body, it)
                    } ?: stringResource(R.string.radio_add_success_body_default)
                )
                RadioHomeStatus.Playing -> {
                    currentStation(uiState)?.let { station ->
                        NowPlayingBanner(
                            station = station,
                            playbackState = uiState.playbackState
                        )
                    }
                }
                RadioHomeStatus.Ready,
                RadioHomeStatus.Empty -> Unit
            }

            if (uiState.savedStations.isNotEmpty()) {
                SectionTitle(text = stringResource(R.string.radio_saved_stations_title))
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = true),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.savedStations,
                        key = { station -> station.id }
                    ) { station ->
                        RadioStationRow(
                            station = station,
                            isPlaying = station.id == uiState.currentItemId &&
                                uiState.playbackState == PlaybackStateModel.Playing,
                            onPlay = { onPlayStation(station) },
                            onToggleFavorite = { onToggleFavorite(station) }
                        )
                    }
                }
            } else {
                EmptyStateCard(
                    onAddStation = onAddStation,
                    suggestedStations = uiState.stations.take(3),
                    onPlayStation = onPlayStation,
                    onToggleFavorite = onToggleFavorite
                )
            }
        }

        uiState.quickAddEditor?.let { editor ->
            QuickAddDialog(
                editor = editor,
                onEditorChange = onQuickAddChange,
                onDismiss = onDismissQuickAdd,
                onSave = onSaveQuickAdd
            )
        }

        uiState.stationEditor?.let { editor ->
            RadioStationEditorDialog(
                editor = editor,
                onEditorChange = onStationEditorChange,
                onDismiss = onDismissStationEditor,
                onSave = onSaveStation
            )
        }
    }
}

@Composable
private fun HeaderBar(
    isLoading: Boolean,
    onAddStation: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.radio_home_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isLoading) {
                    stringResource(R.string.radio_home_loading_hint)
                } else {
                    stringResource(R.string.radio_home_subtitle)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB8C2CF)
            )
        }

        OutlinedButton(onClick = onAddStation) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color(0xFFE0B45C)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = stringResource(R.string.radio_add_favorite_button))
        }
    }
}

@Composable
private fun LoadingCard() {
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
private fun ErrorCard(
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF2A1820),
        border = BorderStroke(1.dp, Color(0x66D59A92))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFF4C8BF),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message,
                color = Color(0xFFF6F1E8)
            )
            Button(onClick = onAction) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun SuccessCard(
    title: String,
    message: String
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF14231B),
        border = BorderStroke(1.dp, Color(0x3348C07F))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFCFF3DE),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = message,
                color = Color(0xFFF6F1E8)
            )
        }
    }
}

@Composable
private fun NowPlayingBanner(
    station: RadioStation,
    playbackState: PlaybackStateModel
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x33E0B45C))
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE0B45C), Color(0xFFAA6A3E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = station.name.take(1).ifBlank { "R" }.uppercase(Locale.ROOT),
                    color = Color(0xFF0B1018),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.radio_now_playing_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFE0B45C),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when (playbackState) {
                        PlaybackStateModel.Buffering,
                        PlaybackStateModel.Reconnecting -> stringResource(R.string.radio_now_playing_buffering)
                        PlaybackStateModel.Playing -> stringResource(R.string.radio_now_playing_active)
                        PlaybackStateModel.Paused -> stringResource(R.string.radio_now_playing_paused)
                        else -> stringResource(R.string.radio_now_playing_active)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB8C2CF)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFFF6F1E8),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun EmptyStateCard(
    onAddStation: () -> Unit,
    suggestedStations: List<RadioStation>,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_empty_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.radio_empty_body),
                color = Color(0xFFD0C4B1)
            )

            Button(onClick = onAddStation) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.radio_add_favorite_button))
            }

            if (suggestedStations.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.radio_empty_starters_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    suggestedStations.forEach { station ->
                        RadioStationRow(
                            station = station,
                            isPlaying = false,
                            onPlay = { onPlayStation(station) },
                            onToggleFavorite = { onToggleFavorite(station) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioStationRow(
    station: RadioStation,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val initials = station.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "R" }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (isPlaying) Color(0xFF182B20) else Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, if (isPlaying) Color(0x6648C07F) else Color(0x1FE0B45C)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlay)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
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
                    color = Color(0xFF0B1018),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = station.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFF6F1E8),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (isPlaying) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0x3348C07F),
                            border = BorderStroke(1.dp, Color(0x6648C07F))
                        ) {
                            Text(
                                text = stringResource(R.string.radio_now_playing_badge),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = Color(0xFFEAF8EF),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (station.isFavorite) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0x33E0B45C),
                            border = BorderStroke(1.dp, Color(0x66E0B45C))
                        ) {
                            Text(
                                text = stringResource(R.string.radio_saved_badge),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = Color(0xFFF6F1E8),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = station.country ?: station.language ?: stringResource(R.string.radio_country_unknown),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD0C4B1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (station.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = stringResource(
                        if (station.isFavorite) {
                            R.string.radio_unfavorite_button
                        } else {
                            R.string.radio_favorite_button
                        }
                    ),
                    tint = if (station.isFavorite) Color(0xFFE0B45C) else Color(0xFFB8C2CF)
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

@Composable
private fun QuickAddDialog(
    editor: RadioQuickAddState,
    onEditorChange: (RadioQuickAddState) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.radio_quick_add_title))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = editor.name,
                    onValueChange = { onEditorChange(editor.copy(name = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_quick_add_name_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.streamUrl,
                    onValueChange = { onEditorChange(editor.copy(streamUrl = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_quick_add_stream_url_label)) },
                    singleLine = true
                )

                editor.validationError?.let { error ->
                    Text(
                        text = error,
                        color = Color(0xFFF4C8BF),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onSave) {
                Text(text = stringResource(R.string.radio_quick_add_save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.radio_quick_add_cancel_button))
            }
        }
    )
}

@Composable
private fun RadioStationEditorDialog(
    editor: RadioStationEditorState,
    onEditorChange: (RadioStationEditorState) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editor.stationId == null) {
                    stringResource(R.string.radio_station_add_title)
                } else {
                    stringResource(R.string.radio_station_edit_title)
                }
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = editor.name,
                    onValueChange = { onEditorChange(editor.copy(name = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_name_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.streamUrl,
                    onValueChange = { onEditorChange(editor.copy(streamUrl = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_stream_url_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.homepageUrl,
                    onValueChange = { onEditorChange(editor.copy(homepageUrl = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_homepage_url_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.artworkUrl,
                    onValueChange = { onEditorChange(editor.copy(artworkUrl = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_artwork_url_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.country,
                    onValueChange = { onEditorChange(editor.copy(country = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_country_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.language,
                    onValueChange = { onEditorChange(editor.copy(language = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_language_label)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = editor.genre,
                    onValueChange = { onEditorChange(editor.copy(genre = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.radio_station_genre_label)) },
                    singleLine = true
                )

                editor.validationError?.let { error ->
                    Text(
                        text = error,
                        color = Color(0xFFF4C8BF),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onSave) {
                Text(text = stringResource(R.string.radio_station_save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.radio_station_cancel_button))
            }
        }
    )
}

private fun currentStation(uiState: com.learpc.learpc.feature.radio.ui.model.RadioUiState): RadioStation? {
    val currentItemId = uiState.currentItemId ?: return null
    return uiState.stations.firstOrNull { it.id == currentItemId }
        ?: uiState.savedStations.firstOrNull { it.id == currentItemId }
}
