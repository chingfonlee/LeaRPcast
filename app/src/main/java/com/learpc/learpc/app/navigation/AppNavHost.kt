package com.learpc.learpc.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.learpc.learpc.feature.downloads.ui.screen.DownloadsScreen
import com.learpc.learpc.feature.player.ui.screen.PlayerScreen
import com.learpc.learpc.feature.podcast.ui.screen.PodcastListScreen
import com.learpc.learpc.feature.radio.ui.screen.RadioScreen
import com.learpc.learpc.feature.settings.ui.screen.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.RADIO,
        modifier = modifier
    ) {
        composable(AppDestinations.RADIO) { RadioScreen() }
        composable(AppDestinations.PODCAST) { PodcastListScreen() }
        composable(AppDestinations.DOWNLOADS) { DownloadsScreen() }
        composable(AppDestinations.SETTINGS) { SettingsScreen() }
        composable(AppDestinations.PLAYER) { PlayerScreen() }
    }
}
