@file:OptIn(ExperimentalLayoutApi::class)

package com.learpc.learpc.feature.radio.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.learpc.learpc.feature.radio.ui.model.RadioBrowseSortMode
import com.learpc.learpc.feature.radio.ui.model.RadioHomeStatus
import com.learpc.learpc.feature.radio.ui.model.RadioShellTab
import com.learpc.learpc.feature.radio.ui.model.RadioStationsTaxonomyMode
import com.learpc.learpc.feature.radio.ui.model.RadioUiState
import com.learpc.learpc.feature.radio.ui.model.labelResId
import java.net.URI
import java.util.Locale

private val RadioShellBackdrop = Brush.verticalGradient(
    colors = listOf(Color(0xFF08121E), Color(0xFF101B2B), Color(0xFF20131F))
)

private const val TAIWAN_COUNTRY = "taiwan"
private const val TAIWAN_COUNTRY_CODE = "tw"

@Composable
fun RadioShellScreen(
    uiState: RadioUiState,
    onShellTabSelected: (RadioShellTab) -> Unit,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onAddStation: () -> Unit,
    onRetryLoad: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onBrowseSortModeChange: (RadioBrowseSortMode) -> Unit,
    onStationsTaxonomyModeChange: (RadioStationsTaxonomyMode) -> Unit,
    onRetryPlayback: () -> Unit,
    onTogglePlayback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentStation = currentStation(uiState)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RadioShellBackdrop)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            RadioShellHeader()

            RadioShellTabs(
                selectedTab = uiState.selectedShellTab,
                onShellTabSelected = onShellTabSelected
            )

            RadioShellStatusBanner(
                uiState = uiState,
                currentStation = currentStation,
                onRetryLoad = onRetryLoad,
                onRetryPlayback = onRetryPlayback
            )

            if (currentStation != null && uiState.selectedShellTab != RadioShellTab.Player) {
                RadioNowPlayingStrip(
                    station = currentStation,
                    playbackState = uiState.playbackState,
                    onOpenPlayer = { onShellTabSelected(RadioShellTab.Player) },
                    onTogglePlayback = onTogglePlayback,
                    onToggleFavorite = { onToggleFavorite(currentStation) }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (uiState.selectedShellTab) {
                    RadioShellTab.Home -> RadioHomeDestination(
                        uiState = uiState,
                        currentStation = currentStation,
                        onShellTabSelected = onShellTabSelected,
                        onPlayStation = onPlayStation,
                        onToggleFavorite = onToggleFavorite,
                        onAddStation = onAddStation
                    )

                    RadioShellTab.Stations -> RadioStationsDestination(
                        uiState = uiState,
                        onPlayStation = onPlayStation,
                        onToggleFavorite = onToggleFavorite,
                        onBrowseSortModeChange = onBrowseSortModeChange,
                        onStationsTaxonomyModeChange = onStationsTaxonomyModeChange
                    )

                    RadioShellTab.Search -> RadioSearchDestination(
                        uiState = uiState,
                        onSearchQueryChange = onSearchQueryChange,
                        onPlayStation = onPlayStation,
                        onToggleFavorite = onToggleFavorite
                    )

                    RadioShellTab.Favorites -> RadioFavoritesDestination(
                        uiState = uiState,
                        onShellTabSelected = onShellTabSelected,
                        onPlayStation = onPlayStation,
                        onToggleFavorite = onToggleFavorite,
                        onAddStation = onAddStation
                    )

                    RadioShellTab.Player -> RadioPlayerDestination(
                        uiState = uiState,
                        currentStation = currentStation,
                        onPlayStation = onPlayStation,
                        onToggleFavorite = onToggleFavorite,
                        onTogglePlayback = onTogglePlayback,
                        onRetryPlayback = onRetryPlayback
                    )
                }
            }
        }
    }
}

@Composable
private fun RadioShellHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = stringResource(R.string.radio_home_title),
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFF6F1E8),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(R.string.radio_home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB8C2CF)
        )
    }
}

@Composable
private fun RadioShellTabs(
    selectedTab: RadioShellTab,
    onShellTabSelected: (RadioShellTab) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = Color.Transparent,
        contentColor = Color(0xFFF6F1E8),
        edgePadding = 0.dp,
        divider = {}
    ) {
        RadioShellTab.entries.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onShellTabSelected(tab) },
                text = {
                    Text(
                        text = stringResource(tab.labelResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun RadioShellStatusBanner(
    uiState: RadioUiState,
    currentStation: RadioStation?,
    onRetryLoad: () -> Unit,
    onRetryPlayback: () -> Unit
) {
    when (uiState.homeStatus) {
        RadioHomeStatus.Loading -> StatusCard(
            title = stringResource(R.string.radio_shell_loading_title),
            message = stringResource(R.string.radio_shell_loading_body),
            accent = Color(0xFFE0B45C),
            showProgress = true
        )

        RadioHomeStatus.NetworkFailure -> StatusCard(
            title = stringResource(R.string.radio_shell_network_failure_title),
            message = uiState.networkErrorMessage
                ?: stringResource(R.string.radio_shell_network_failure_body),
            accent = Color(0xFFD59A92),
            actionLabel = stringResource(R.string.radio_shell_retry_button),
            actionIcon = Icons.Filled.Refresh,
            onAction = onRetryLoad
        )

        RadioHomeStatus.PlaybackFailure -> {
            val stationName = currentStation?.name
                ?: stringResource(R.string.radio_player_station_name_label)
            StatusCard(
                title = stringResource(R.string.radio_player_state_failed),
                message = uiState.playbackErrorMessage
                    ?: stringResource(R.string.radio_playback_failure_body),
                supportingText = stationName,
                accent = Color(0xFFD59A92),
                actionLabel = stringResource(R.string.radio_player_retry_button),
                actionIcon = Icons.Filled.Refresh,
                onAction = onRetryPlayback
            )
        }

        RadioHomeStatus.AddSuccess -> StatusCard(
            title = stringResource(R.string.radio_add_success_title),
            message = uiState.addSuccessMessage?.let {
                stringResource(R.string.radio_add_success_body, it)
            } ?: stringResource(R.string.radio_add_success_body_default),
            accent = Color(0xFF48C07F)
        )

        RadioHomeStatus.Ready,
        RadioHomeStatus.Empty,
        RadioHomeStatus.Playing -> Unit
    }
}

@Composable
private fun StatusCard(
    title: String,
    message: String,
    accent: Color,
    supportingText: String? = null,
    actionLabel: String? = null,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    showProgress: Boolean = false,
    onAction: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.30f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (showProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = accent
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (supportingText != null) {
                Text(text = supportingText, color = Color(0xFFB8C2CF))
            }

            Text(text = message, color = Color(0xFFD0C4B1))

            if (actionLabel != null && onAction != null) {
                OutlinedButton(onClick = onAction) {
                    if (actionIcon != null) {
                        Icon(imageVector = actionIcon, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@Composable
private fun RadioNowPlayingStrip(
    station: RadioStation,
    playbackState: PlaybackStateModel,
    onOpenPlayer: () -> Unit,
    onTogglePlayback: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x33E0B45C)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenPlayer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE0B45C), Color(0xFFAA6A3E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stationInitials(station),
                    color = Color(0xFF0B1018),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.radio_player_now_playing_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFE0B45C),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = playbackStateLabel(playbackState),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB8C2CF)
                )
            }

            IconButton(onClick = onTogglePlayback) {
                Icon(
                    imageVector = if (playbackState == PlaybackStateModel.Playing) {
                        Icons.Filled.Pause
                    } else {
                        Icons.Filled.PlayArrow
                    },
                    contentDescription = stringResource(
                        if (playbackState == PlaybackStateModel.Playing) {
                            R.string.radio_player_pause_button
                        } else {
                            R.string.radio_player_play_button
                        }
                    ),
                    tint = Color(0xFFE0B45C)
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
        }
    }
}

@Composable
private fun RadioHomeDestination(
    uiState: RadioUiState,
    currentStation: RadioStation?,
    onShellTabSelected: (RadioShellTab) -> Unit,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onAddStation: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            if (uiState.savedStations.isEmpty() && uiState.recentStations.isEmpty() && currentStation == null) {
                HomeEmptyStateCard(
                    onAddStation = onAddStation,
                    onBrowseStations = { onShellTabSelected(RadioShellTab.Stations) }
                )
            } else {
                HomeShortcutCard(
                    onBrowseStations = { onShellTabSelected(RadioShellTab.Stations) },
                    onSearch = { onShellTabSelected(RadioShellTab.Search) },
                    onFavorites = { onShellTabSelected(RadioShellTab.Favorites) }
                )
            }
        }

        if (currentStation != null) {
            item {
                SectionHeader(text = stringResource(R.string.radio_player_now_playing_label))
                RadioStationRowShell(
                    station = currentStation,
                    isActive = true,
                    onPlay = { onPlayStation(currentStation) },
                    onToggleFavorite = { onToggleFavorite(currentStation) }
                )
            }
        }

        if (uiState.savedStations.isNotEmpty()) {
            item {
                SectionHeader(text = stringResource(R.string.radio_shell_saved_title))
            }
            items(
                items = uiState.savedStations,
                key = { it.id }
            ) { station ->
                RadioStationRowShell(
                    station = station,
                    isActive = currentStation?.id == station.id,
                    onPlay = { onPlayStation(station) },
                    onToggleFavorite = { onToggleFavorite(station) }
                )
            }
        } else if (currentStation == null) {
            item {
                EmptyHintCard(
                    title = stringResource(R.string.radio_home_empty_title),
                    body = stringResource(R.string.radio_home_empty_body)
                )
            }
        }

        if (uiState.recentStations.isNotEmpty()) {
            item {
                SectionHeader(text = stringResource(R.string.radio_shell_recent_title))
            }
            items(
                items = uiState.recentStations,
                key = { it.id }
            ) { station ->
                RadioStationRowShell(
                    station = station,
                    isActive = currentStation?.id == station.id,
                    onPlay = { onPlayStation(station) },
                    onToggleFavorite = { onToggleFavorite(station) }
                )
            }
        }
    }
}

@Composable
private fun HomeEmptyStateCard(
    onAddStation: () -> Unit,
    onBrowseStations: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_home_empty_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.radio_home_empty_body),
                color = Color(0xFFD0C4B1)
            )
            Button(onClick = onAddStation) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.radio_home_empty_primary_action))
            }
            OutlinedButton(onClick = onBrowseStations) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.radio_home_empty_secondary_action))
            }
        }
    }
}

@Composable
private fun HomeShortcutCard(
    onBrowseStations: () -> Unit,
    onSearch: () -> Unit,
    onFavorites: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_shell_catalog_title),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ActionChip(
                    label = stringResource(R.string.radio_shell_catalog_stations_button),
                    onClick = onBrowseStations
                )
                ActionChip(
                    label = stringResource(R.string.radio_shell_catalog_search_button),
                    onClick = onSearch
                )
                ActionChip(
                    label = stringResource(R.string.radio_shell_catalog_favorites_button),
                    onClick = onFavorites
                )
            }
        }
    }
}

@Composable
private fun ActionChip(
    label: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = { Text(text = label) }
    )
}

@Composable
private fun RadioStationsDestination(
    uiState: RadioUiState,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onBrowseSortModeChange: (RadioBrowseSortMode) -> Unit,
    onStationsTaxonomyModeChange: (RadioStationsTaxonomyMode) -> Unit
) {
    val stations = uiState.stations
    val taiwanStations = stations.filter(::isTaiwanStation)
    val activeStationId = uiState.currentItemId
    val sortedStations = sortStations(taiwanStations, uiState.browseSortMode)
    val otherLabel = stringResource(R.string.radio_shell_other_group)
    val taxonomyMode = uiState.stationsTaxonomyMode

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.radio_shell_taiwan_stations_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            StationsTaxonomyModeRow(
                selectedMode = taxonomyMode,
                onStationsTaxonomyModeChange = onStationsTaxonomyModeChange
            )
        }

        item {
            SortChipRow(
                selectedSortMode = uiState.browseSortMode,
                onBrowseSortModeChange = onBrowseSortModeChange
            )
        }

        if (taiwanStations.isEmpty()) {
            item {
                EmptyHintCard(
                    title = stringResource(R.string.radio_shell_taiwan_empty_title),
                    body = stringResource(R.string.radio_shell_taiwan_empty_body)
                )
            }
        } else {
            item {
                StationGroupingSection(
                    title = when (taxonomyMode) {
                        RadioStationsTaxonomyMode.Categories ->
                            stringResource(R.string.radio_shell_stations_categories_title)
                        RadioStationsTaxonomyMode.Networks ->
                            stringResource(R.string.radio_shell_stations_networks_title)
                        RadioStationsTaxonomyMode.Regions ->
                            stringResource(R.string.radio_shell_stations_regions_title)
                    },
                    groupedStations = when (taxonomyMode) {
                        RadioStationsTaxonomyMode.Categories -> groupStationsByTaxonomyLabel(
                            stations = sortedStations,
                            classifier = { taxonomyCategoryLabel(it) },
                            fallbackLabel = otherLabel
                        )
                        RadioStationsTaxonomyMode.Networks -> groupStationsByTaxonomyLabel(
                            stations = sortedStations,
                            classifier = { taxonomyNetworkLabel(it) },
                            fallbackLabel = otherLabel
                        )
                        RadioStationsTaxonomyMode.Regions -> groupStationsByTaxonomyLabel(
                            stations = sortedStations,
                            classifier = { taxonomyRegionLabel(it) },
                            fallbackLabel = otherLabel
                        )
                    },
                    activeStationId = activeStationId,
                    onPlayStation = onPlayStation,
                    onToggleFavorite = onToggleFavorite
                )
            }
        }
    }
}

@Composable
private fun StationsTaxonomyModeRow(
    selectedMode: RadioStationsTaxonomyMode,
    onStationsTaxonomyModeChange: (RadioStationsTaxonomyMode) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RadioStationsTaxonomyMode.entries.forEach { mode ->
            FilterChip(
                selected = selectedMode == mode,
                onClick = { onStationsTaxonomyModeChange(mode) },
                label = {
                    Text(
                        text = stringResource(mode.labelResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun SortChipRow(
    selectedSortMode: RadioBrowseSortMode,
    onBrowseSortModeChange: (RadioBrowseSortMode) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RadioBrowseSortMode.entries.forEach { mode ->
            FilterChip(
                selected = selectedSortMode == mode,
                onClick = { onBrowseSortModeChange(mode) },
                label = {
                    Text(
                        text = stringResource(mode.labelResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
private fun StationGroupingSection(
    title: String,
    groupedStations: List<StationGroup>,
    activeStationId: String?,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(text = title)
        groupedStations.forEach { group ->
            if (group.stations.isNotEmpty()) {
                GroupCard(
                    title = group.title,
                    stations = group.stations,
                    activeStationId = activeStationId,
                    onPlayStation = onPlayStation,
                    onToggleFavorite = onToggleFavorite
                )
            }
        }
    }
}

@Composable
private fun GroupCard(
    title: String,
    stations: List<RadioStation>,
    activeStationId: String?,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            stations.take(6).forEach { station ->
                RadioStationRowShell(
                    station = station,
                    isActive = station.id == activeStationId,
                    onPlay = { onPlayStation(station) },
                    onToggleFavorite = { onToggleFavorite(station) }
                )
            }
        }
    }
}

@Composable
private fun RadioSearchDestination(
    uiState: RadioUiState,
    onSearchQueryChange: (String) -> Unit,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit
) {
    val query = uiState.searchQuery
    val results = uiState.searchResults

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.radio_shell_tab_search),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(R.string.radio_shell_search_hint)) },
                placeholder = {
                    Text(text = stringResource(R.string.radio_shell_search_placeholder))
                },
                singleLine = true
            )
        }

        item {
            Text(
                text = stringResource(R.string.radio_shell_search_helper),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB8C2CF)
            )
        }

        if (query.isBlank()) {
            item {
                EmptyHintCard(
                    title = stringResource(R.string.radio_shell_search_empty_title),
                    body = stringResource(R.string.radio_shell_search_prompt_body)
                )
            }
        } else if (results.isEmpty()) {
            item {
                SearchEmptyStateCard(query = query, onClearSearch = { onSearchQueryChange("") })
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.radio_shell_search_results_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFF6F1E8),
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(
                items = results,
                key = { it.id }
            ) { station ->
                RadioStationRowShell(
                    station = station,
                    isActive = station.id == uiState.currentItemId,
                    onPlay = { onPlayStation(station) },
                    onToggleFavorite = { onToggleFavorite(station) }
                )
            }
        }
    }
}

@Composable
private fun SearchEmptyStateCard(
    query: String,
    onClearSearch: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_search_empty_title, query),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.radio_search_empty_body),
                color = Color(0xFFD0C4B1)
            )
            OutlinedButton(onClick = onClearSearch) {
                Text(text = stringResource(R.string.radio_search_empty_clear_action))
            }
        }
    }
}

@Composable
private fun RadioFavoritesDestination(
    uiState: RadioUiState,
    onShellTabSelected: (RadioShellTab) -> Unit,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onAddStation: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.radio_shell_tab_favorites),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
        }

        if (uiState.savedStations.isEmpty()) {
            item {
                FavoritesEmptyStateCard(
                    onBrowseStations = { onShellTabSelected(RadioShellTab.Stations) },
                    onAddStation = onAddStation
                )
            }
        } else {
            items(
                items = uiState.savedStations,
                key = { it.id }
            ) { station ->
                RadioStationRowShell(
                    station = station,
                    isActive = station.id == uiState.currentItemId,
                    onPlay = { onPlayStation(station) },
                    onToggleFavorite = { onToggleFavorite(station) }
                )
            }
        }
    }
}

@Composable
private fun FavoritesEmptyStateCard(
    onBrowseStations: () -> Unit,
    onAddStation: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_favorites_empty_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.radio_favorites_empty_body),
                color = Color(0xFFD0C4B1)
            )
            Button(onClick = onBrowseStations) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.radio_favorites_empty_primary_action))
            }
            TextButton(onClick = onAddStation) {
                Text(text = stringResource(R.string.radio_home_empty_primary_action))
            }
        }
    }
}

@Composable
private fun RadioPlayerDestination(
    uiState: RadioUiState,
    currentStation: RadioStation?,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onTogglePlayback: () -> Unit,
    onRetryPlayback: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.radio_shell_tab_player),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
        }

        if (currentStation == null) {
            item {
                EmptyHintCard(
                    title = stringResource(R.string.radio_player_station_name_label),
                    body = stringResource(R.string.radio_shell_search_prompt_body)
                )
            }
        } else {
            item {
                FullPlayerCard(
                    station = currentStation,
                    playbackState = uiState.playbackState,
                    onPlayStation = onPlayStation,
                    onToggleFavorite = onToggleFavorite,
                    onTogglePlayback = onTogglePlayback,
                    onRetryPlayback = onRetryPlayback
                )
            }
        }
    }
}

@Composable
private fun FullPlayerCard(
    station: RadioStation,
    playbackState: PlaybackStateModel,
    onPlayStation: (RadioStation) -> Unit,
    onToggleFavorite: (RadioStation) -> Unit,
    onTogglePlayback: () -> Unit,
    onRetryPlayback: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x33E0B45C))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.radio_player_now_playing_label),
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFE0B45C),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = station.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = metadataLine(station),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB8C2CF)
            )

            Text(
                text = playbackStateLabel(playbackState),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD0C4B1)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onTogglePlayback) {
                    Icon(
                        imageVector = if (playbackState == PlaybackStateModel.Playing) {
                            Icons.Filled.Pause
                        } else {
                            Icons.Filled.PlayArrow
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(
                            if (playbackState == PlaybackStateModel.Playing) {
                                R.string.radio_player_pause_button
                            } else {
                                R.string.radio_player_play_button
                            }
                        )
                    )
                }

                OutlinedButton(onClick = { onToggleFavorite(station) }) {
                    Icon(
                        imageVector = if (station.isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Filled.FavoriteBorder
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(
                            if (station.isFavorite) {
                                R.string.radio_player_unfavorite_button
                            } else {
                                R.string.radio_player_favorite_button
                            }
                        )
                    )
                }
            }

            if (playbackState is PlaybackStateModel.Error) {
                HorizontalDivider(color = Color(0x33D59A92))
                Text(
                    text = stringResource(R.string.radio_player_state_failed),
                    color = Color(0xFFF4C8BF)
                )
                Button(onClick = onRetryPlayback) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(R.string.radio_player_retry_button))
                }
            }
        }
    }
}

@Composable
private fun RadioStationRowShell(
    station: RadioStation,
    isActive: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = if (isActive) Color(0xFF182B20) else Color(0xFF0F1C2A),
        border = BorderStroke(
            1.dp,
            if (isActive) Color(0x6648C07F) else Color(0x1FE0B45C)
        ),
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
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE0B45C), Color(0xFFAA6A3E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stationInitials(station),
                    color = Color(0xFF0B1018),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    if (isActive) {
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
                    text = metadataLine(station),
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
private fun EmptyHintCard(
    title: String,
    body: String
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF0F1C2A),
        border = BorderStroke(1.dp, Color(0x1FE0B45C))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFF6F1E8),
                fontWeight = FontWeight.SemiBold
            )
            Text(text = body, color = Color(0xFFD0C4B1))
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFFF6F1E8),
        fontWeight = FontWeight.SemiBold
    )
}

private data class StationGroup(
    val title: String,
    val stations: List<RadioStation>
)

private fun currentStation(uiState: RadioUiState): RadioStation? {
    val currentItemId = uiState.currentItemId ?: return null
    return uiState.stations.firstOrNull { it.id == currentItemId }
        ?: uiState.savedStations.firstOrNull { it.id == currentItemId }
        ?: uiState.recentStations.firstOrNull { it.id == currentItemId }
}

private fun stationInitials(station: RadioStation): String {
    return station.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "R" }
}

@Composable
private fun playbackStateLabel(state: PlaybackStateModel): String {
    return when (state) {
        PlaybackStateModel.Buffering,
        PlaybackStateModel.Reconnecting -> stringResource(R.string.radio_player_state_buffering)
        PlaybackStateModel.Playing -> stringResource(R.string.radio_player_state_playing)
        PlaybackStateModel.Paused -> stringResource(R.string.radio_player_state_paused)
        PlaybackStateModel.Idle -> stringResource(R.string.radio_loading)
        is PlaybackStateModel.Error -> stringResource(R.string.radio_player_state_failed)
    }
}

@Composable
private fun metadataLine(station: RadioStation): String {
    val parts = listOfNotNull(
        station.displayFrequency?.takeIf { it.isNotBlank() }
            ?: listOfNotNull(station.band, station.frequency)
                .joinToString(" ")
                .takeIf { it.isNotBlank() },
        station.network?.takeIf { it.isNotBlank() },
        station.region?.takeIf { it.isNotBlank() },
        station.uiPrimaryGroup?.takeIf { it.isNotBlank() },
        station.language?.takeIf { it.isNotBlank() },
        station.codec?.takeIf { it.isNotBlank() },
        station.bitrateKbps?.let { "${it}kbps" }
    )
    return if (parts.isNotEmpty()) {
        parts.joinToString(" ? ")
    } else {
        stringResource(R.string.radio_country_unknown)
    }
}

private fun groupStationsByTaxonomyLabel(
    stations: List<RadioStation>,
    classifier: (RadioStation) -> String?,
    fallbackLabel: String
): List<StationGroup> {
    return stations
        .groupBy { classifier(it)?.trim().takeUnless { value -> value.isNullOrBlank() } ?: fallbackLabel }
        .entries
        .sortedWith(
            compareByDescending<Map.Entry<String, List<RadioStation>>> { it.value.size }
                .thenBy { it.key.lowercase(Locale.ROOT) }
        )
        .map { (title, groupedStations) ->
            StationGroup(
                title = title,
                stations = groupedStations.sortedBy { it.name.lowercase(Locale.ROOT) }
            )
        }
}

private fun sortStations(
    stations: List<RadioStation>,
    sortMode: RadioBrowseSortMode
): List<RadioStation> {
    return when (sortMode) {
        RadioBrowseSortMode.Recent -> stations.sortedWith(
            compareByDescending<RadioStation> { it.sortOrder ?: Int.MIN_VALUE }
                .thenBy { it.name.lowercase(Locale.ROOT) }
        )

        RadioBrowseSortMode.Name -> stations.sortedBy { it.name.lowercase(Locale.ROOT) }

        RadioBrowseSortMode.Frequency -> stations.sortedWith(
            compareBy<RadioStation> { frequencySortKey(it) }
                .thenBy { it.name.lowercase(Locale.ROOT) }
        )
    }
}

private fun frequencySortKey(station: RadioStation): Double {
    val nameFrequency = Regex("""\d+(?:\.\d+)?""")
        .find(station.name)
        ?.value
        ?.toDoubleOrNull()
    val codecPriority = station.codec?.lowercase(Locale.ROOT)?.let {
        when {
            it.contains("aac") -> 1.0
            it.contains("mp3") -> 2.0
            it.contains("ogg") -> 3.0
            else -> 4.0
        }
    } ?: 5.0
    return nameFrequency ?: codecPriority + ((station.bitrateKbps ?: 0).toDouble() / 10_000.0)
}

private fun stationStreamHost(station: RadioStation): String? {
    val url = station.resolvedStreamUrl ?: station.streamUrl
    return runCatching {
        URI(url).host?.takeIf { it.isNotBlank() }
    }.getOrNull()
}

private fun taxonomyNetworkLabel(station: RadioStation): String? {
    station.network?.trim()?.takeIf { it.isNotBlank() }?.let { return it }

    return stationStreamHost(station)
        ?.removePrefix("www.")
        ?.substringBefore('.')
        ?.takeIf { it.isNotBlank() }
        ?.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.ROOT) else char.toString()
        }
}

private fun taxonomyRegionLabel(station: RadioStation): String? {
    station.region?.trim()?.takeIf { it.isNotBlank() }?.let { return it }

    val country = station.country?.trim()?.takeIf { it.isNotBlank() }
    val countryCode = station.countryCode?.trim()?.takeIf { it.isNotBlank() }
    return country ?: countryCode ?: "Unknown"
}

private fun taxonomyCategoryLabel(station: RadioStation): String? {
    station.uiPrimaryGroup?.trim()?.takeIf { it.isNotBlank() }?.let { return it }
    station.category?.trim()?.takeIf { it.isNotBlank() }?.let { return it }
    return station.genre?.takeIf { it.isNotBlank() }
}

private fun isTaiwanStation(station: RadioStation): Boolean {
    val country = station.country?.trim()?.lowercase(Locale.ROOT)
    val countryCode = station.countryCode?.trim()?.lowercase(Locale.ROOT)
    return country == TAIWAN_COUNTRY || countryCode == TAIWAN_COUNTRY_CODE
}

private val TAIWAN_CATEGORY_LABELS = listOf(
    "news" to "News & Talk",
    "talk" to "News & Talk",
    "commentary" to "News & Talk",
    "music" to "Music",
    "pop" to "Music",
    "song" to "Music",
    "public" to "Public Service",
    "service" to "Public Service",
    "education" to "Education & Campus",
    "campus" to "Education & Campus",
    "language" to "Language & Culture",
    "culture" to "Language & Culture",
    "religion" to "Religion & Spiritual",
    "spiritual" to "Religion & Spiritual",
    "local" to "Local / General",
    "community" to "Local / General",
    "internet" to "Online Music"
)

private val TAIWAN_NETWORK_LABELS = listOf(
    "中廣" to "中廣",
    "bcc" to "中廣",
    "asiafm" to "ASIAFM",
    "寶島" to "寶島聯播網",
    "微微笑" to "微微笑廣播網",
    "警廣" to "警察廣播電臺",
    "icrt" to "ICRT",
    "hit fm" to "Hit FM",
    "hitfm" to "Hit FM",
    "news98" to "News98",
    "989" to "News98",
    "飛揚" to "飛揚調頻",
    "kiss radio" to "KISS RADIO",
    "kissradio" to "KISS RADIO",
    "pop radio" to "POP Radio",
    "popradio" to "POP Radio"
)

private val TAIWAN_REGION_LABELS = listOf(
    "台北" to "台北",
    "臺北" to "台北",
    "新北" to "新北",
    "桃園" to "桃園",
    "新竹" to "新竹",
    "苗栗" to "苗栗",
    "台中" to "台中",
    "臺中" to "台中",
    "彰化" to "彰化",
    "南投" to "南投",
    "雲林" to "雲林",
    "嘉義" to "嘉義",
    "台南" to "台南",
    "臺南" to "台南",
    "高雄" to "高雄",
    "屏東" to "屏東",
    "宜蘭" to "宜蘭",
    "花蓮" to "花蓮",
    "台東" to "台東",
    "臺東" to "台東",
    "澎湖" to "澎湖",
    "金門" to "金門",
    "馬祖" to "馬祖"
)

private fun <T> List<T>.ifNotEmptyOr(fallback: List<T>): List<T> {
    return if (isNotEmpty()) this else fallback
}
