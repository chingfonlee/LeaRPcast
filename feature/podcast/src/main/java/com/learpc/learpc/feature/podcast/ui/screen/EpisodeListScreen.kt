package com.learpc.learpc.feature.podcast.ui.screen

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
import androidx.compose.material.icons.filled.GraphicEq
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
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.feature.podcast.R
import com.learpc.learpc.feature.podcast.ui.viewmodel.PodcastDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val EpisodeBackground = Brush.verticalGradient(
    colors = listOf(Color(0xFFF7F0E5), Color(0xFFEBDCC8), Color(0xFFD7C19C))
)

@Composable
fun EpisodeListScreen(
    viewModel: PodcastDetailViewModel,
    modifier: Modifier = Modifier
) {
    val podcast by viewModel.podcast.collectAsState()
    val episodeUiState by viewModel.episodeUiState.collectAsState()

    EpisodeListContent(
        podcastTitle = podcast?.title,
        episodes = episodeUiState.episodes,
        currentlyPlayingId = episodeUiState.currentlyPlayingId,
        isLoading = episodeUiState.isLoading,
        errorMessage = episodeUiState.errorMessage,
        onPlayEpisode = viewModel::playEpisode,
        modifier = modifier
    )
}

@Composable
private fun EpisodeListContent(
    podcastTitle: String?,
    episodes: List<Episode>,
    currentlyPlayingId: String?,
    isLoading: Boolean,
    errorMessage: String?,
    onPlayEpisode: (Episode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(EpisodeBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.episode_library_label),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF8B4A24),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = podcastTitle ?: stringResource(R.string.episode_list_title),
                style = MaterialTheme.typography.displaySmall,
                color = Color(0xFF1C1816),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.episode_list_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5247)
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> LoadingEpisodePanel()
                errorMessage != null -> EmptyEpisodePanel(
                    title = stringResource(R.string.episode_error_title),
                    body = errorMessage
                )
                episodes.isEmpty() -> EmptyEpisodePanel(
                    title = stringResource(R.string.episode_empty_title),
                    body = stringResource(R.string.episode_empty_body)
                )
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(episodes, key = { it.id }) { episode ->
                        EpisodeRow(
                            episode = episode,
                            isPlaying = episode.id == currentlyPlayingId,
                            onPlay = { onPlayEpisode(episode) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingEpisodePanel() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF8B4A24)
            )
            Text(
                text = stringResource(R.string.episode_loading),
                color = Color(0xFF4B3E35)
            )
        }
    }
}

@Composable
private fun EmptyEpisodePanel(
    title: String,
    body: String
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1C1816),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5247)
            )
        }
    }
}

@Composable
private fun EpisodeRow(
    episode: Episode,
    isPlaying: Boolean,
    onPlay: () -> Unit
) {
    val borderColor = if (isPlaying) Color(0xFF8B4A24) else Color(0x1A1C1816)
    val backgroundColor = if (isPlaying) Color(0xFFFFF2E2) else Color(0xFFF8F2E8)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        border = BorderStroke(if (isPlaying) 2.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8B4A24)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "EP",
                    color = Color(0xFFFFF5E8),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1C1816),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.episode_description_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F5247),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = formatEpisodeMeta(episode),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6E6258)
                )
            }
            IconButton(onClick = onPlay) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.GraphicEq else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Currently playing" else "Play episode",
                    tint = if (isPlaying) Color(0xFF8B4A24) else Color(0xFF1C1816)
                )
            }
        }
    }
}

private fun formatEpisodeMeta(episode: Episode): String {
    val duration = episode.durationMs?.let { formatDuration(it) } ?: "Duration unavailable"
    val published = episode.publishedAt?.let { formatPublishedDate(it) } ?: "Publishing date unavailable"
    return "$duration - $published"
}

private fun formatPublishedDate(epochMs: Long): String {
    val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.format(Date(epochMs))
}

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%d:%02d", minutes, seconds)
    }
}
