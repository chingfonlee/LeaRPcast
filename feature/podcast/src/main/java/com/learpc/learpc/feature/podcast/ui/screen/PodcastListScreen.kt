package com.learpc.learpc.feature.podcast.ui.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.feature.podcast.R
import com.learpc.learpc.feature.podcast.ui.model.PodcastUiState
import com.learpc.learpc.feature.podcast.ui.viewmodel.PodcastViewModel

private val PodcastBackground = Brush.verticalGradient(
    colors = listOf(Color(0xFFF7F0E5), Color(0xFFEBDCC8), Color(0xFFD7C19C))
)

@Composable
fun PodcastListScreen(
    viewModel: PodcastViewModel,
    onPodcastClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    PodcastListContent(
        uiState = uiState,
        onPodcastClick = onPodcastClick,
        onRefreshPodcast = viewModel::refreshPodcast,
        modifier = modifier
    )
}

@Composable
private fun PodcastListContent(
    uiState: PodcastUiState,
    onPodcastClick: (String) -> Unit,
    onRefreshPodcast: (Podcast) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PodcastBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.podcast_library_label),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF8B4A24),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.podcast_list_title),
                style = MaterialTheme.typography.displaySmall,
                color = Color(0xFF1C1816),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.podcast_list_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5247)
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                uiState.isLoading -> LoadingPanel()
                uiState.errorMessage != null -> EmptyPanel(
                    title = stringResource(R.string.podcast_error_title),
                    body = uiState.errorMessage ?: ""
                )
                uiState.podcasts.isEmpty() -> EmptyPanel(
                    title = stringResource(R.string.podcast_empty_title),
                    body = stringResource(R.string.podcast_empty_body)
                )
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(uiState.podcasts, key = { it.id }) { podcast ->
                            PodcastRow(
                                podcast = podcast,
                                onClick = { onPodcastClick(podcast.id) },
                                onRefresh = { onRefreshPodcast(podcast) }
                            )
                        }
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
                text = stringResource(R.string.podcast_loading),
                color = Color(0xFF4B3E35)
            )
        }
    }
}

@Composable
private fun EmptyPanel(
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
private fun PodcastRow(
    podcast: Podcast,
    onClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val initials = podcast.title
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "P" }

    ElevatedCard(
        shape = RoundedCornerShape(28.dp),
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFF8F2E8)
        ),
        elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF7C3E22), Color(0xFFC1864B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color(0xFFFFF4E7),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1C1816),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = podcast.author ?: podcast.feedUrl,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5F5247),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(text = if (podcast.isActive) "Active" else "Inactive") }
                    )
                    AssistChip(
                        onClick = onRefresh,
                        label = { Text(text = stringResource(R.string.podcast_refresh_feed)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}
