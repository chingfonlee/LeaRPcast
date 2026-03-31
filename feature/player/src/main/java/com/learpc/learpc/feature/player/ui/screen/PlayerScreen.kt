package com.learpc.learpc.feature.player.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.learpc.learpc.feature.player.R
import com.learpc.learpc.feature.player.ui.model.PlayerUiState
import com.learpc.learpc.feature.player.ui.viewmodel.PlayerViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    PlayerContent(
        uiState = uiState,
        onPlayPauseClick = viewModel::playOrPause,
        modifier = modifier
    )
}

@Composable
private fun PlayerContent(
    uiState: PlayerUiState,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = uiState.currentItem?.title ?: stringResource(R.string.player_unknown_title)
    val subtitle = uiState.currentItem?.subtitle

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF07111B),
                        Color(0xFF0F1C2A),
                        Color(0xFF1A1020)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.player_now_playing),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFE0B45C)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(36.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF27384F),
                                    Color(0xFF5C3B2E),
                                    Color(0xFF111923)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.58f)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.10f))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.33f)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFFF6F1E8),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (subtitle != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFD2C5B0),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Column {
                if (uiState.isBuffering) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFE0B45C),
                        trackColor = Color.White.copy(alpha = 0.12f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFFF0D9A6),
                    tonalElevation = 2.dp
                ) {
                    FilledTonalIconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = stringResource(
                                if (uiState.isPlaying) R.string.player_pause_button else R.string.player_play_button
                            )
                        )
                    }
                }
            }
        }
    }
}
