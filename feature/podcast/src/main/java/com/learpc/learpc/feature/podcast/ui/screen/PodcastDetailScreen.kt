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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.feature.podcast.ui.viewmodel.PodcastDetailViewModel
import java.util.Locale

private val DetailBackground = Brush.verticalGradient(
    colors = listOf(Color(0xFFF8F2E8), Color(0xFFE9D7C0), Color(0xFFD9C09D))
)

@Composable
fun PodcastDetailScreen(
    viewModel: PodcastDetailViewModel,
    onOpenEpisodes: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val podcast by viewModel.podcast.collectAsState()
    val episodeUiState by viewModel.episodeUiState.collectAsState()

    PodcastDetailContent(
        podcast = podcast,
        episodes = episodeUiState.episodes,
        isLoading = episodeUiState.isLoading,
        errorMessage = episodeUiState.errorMessage,
        podcastId = viewModel.podcastId,
        onOpenEpisodes = onOpenEpisodes,
        modifier = modifier
    )
}

@Composable
private fun PodcastDetailContent(
    podcast: Podcast?,
    episodes: List<Episode>,
    isLoading: Boolean,
    errorMessage: String?,
    podcastId: String,
    onOpenEpisodes: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DetailBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Podcast detail",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF8B4A24),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            when {
                podcast == null -> LoadingDetailCard()
                else -> DetailHero(
                    podcast = podcast,
                    episodeCount = episodes.size,
                    onOpenEpisodes = { onOpenEpisodes(podcastId) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                ErrorBanner(message = errorMessage)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Recent episodes",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1C1816),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> LoadingEpisodeCard()
                episodes.isEmpty() -> EmptyEpisodeCard()
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(episodes.take(3), key = { it.id }) { episode ->
                        RecentEpisodeRow(episode = episode)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingDetailCard() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF8B4A24)
            )
            Text(
                text = "Loading podcast detail",
                color = Color(0xFF4B3E35)
            )
        }
    }
}

@Composable
private fun LoadingEpisodeCard() {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816))
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF8B4A24)
            )
            Text(text = "Loading episodes")
        }
    }
}

@Composable
private fun EmptyEpisodeCard() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFF8F2E8),
        border = BorderStroke(1.dp, Color(0x1A1C1816))
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "No episodes yet",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1C1816),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Once the feed is refreshed, the latest episodes will appear here in sequence.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5247)
            )
        }
    }
}

@Composable
private fun DetailHero(
    podcast: Podcast,
    episodeCount: Int,
    onOpenEpisodes: () -> Unit
) {
    val initials = podcast.title
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "P" }

    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color(0xFF1C1816)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF8B4A24), Color(0xFFC1864B))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFFF5E8),
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = podcast.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFF8F2E8),
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = podcast.description ?: podcast.feedUrl,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFD8C7B5),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AssistChip(
                    onClick = {},
                    label = { Text(text = "$episodeCount episodes") }
                )
                AssistChip(
                    onClick = onOpenEpisodes,
                    label = { Text(text = "Open episodes") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(text = podcast.author ?: "Uncredited") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onOpenEpisodes) {
                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "Browse full list")
            }
        }
    }
}

@Composable
private fun RecentEpisodeRow(episode: Episode) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFF8F2E8)
        ),
        elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatDuration(episode.durationMs),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6E6258)
                )
            }
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFF6E2DD),
        border = BorderStroke(1.dp, Color(0x55A6543A))
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF8B3B25)
        )
    }
}

private fun formatDuration(durationMs: Long?): String {
    if (durationMs == null) return "Duration unavailable"
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
