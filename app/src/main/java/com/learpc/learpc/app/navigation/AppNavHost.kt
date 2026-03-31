package com.learpc.learpc.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.learpc.learpc.feature.downloads.ui.screen.DownloadsScreen
import com.learpc.learpc.feature.player.ui.screen.PlayerScreen
import com.learpc.learpc.feature.player.ui.viewmodel.PlayerViewModel
import com.learpc.learpc.feature.podcast.navigation.PODCAST_DETAIL_ROUTE
import com.learpc.learpc.feature.podcast.navigation.PODCAST_EPISODES_ROUTE
import com.learpc.learpc.feature.podcast.navigation.PODCAST_ID_ARG
import com.learpc.learpc.feature.podcast.navigation.podcastDetailRoute
import com.learpc.learpc.feature.podcast.navigation.podcastEpisodesRoute
import com.learpc.learpc.feature.podcast.ui.screen.EpisodeListScreen
import com.learpc.learpc.feature.podcast.ui.screen.PodcastDetailScreen
import com.learpc.learpc.feature.podcast.ui.screen.PodcastListScreen
import com.learpc.learpc.feature.radio.ui.viewmodel.RadioViewModel
import com.learpc.learpc.feature.radio.ui.screen.RadioScreen
import com.learpc.learpc.feature.settings.ui.screen.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.RADIO,
        modifier = modifier
    ) {
        composable(AppDestinations.RADIO) {
            RadioScreen(viewModel = hiltViewModel<RadioViewModel>())
        }
        composable(AppDestinations.PODCAST) {
            PodcastListScreen(
                viewModel = hiltViewModel(),
                onPodcastClick = { podcastId ->
                    navController.navigate(podcastDetailRoute(podcastId))
                }
            )
        }
        composable(
            route = PODCAST_DETAIL_ROUTE + "/{$PODCAST_ID_ARG}",
            arguments = listOf(navArgument(PODCAST_ID_ARG) { type = NavType.StringType })
        ) {
            PodcastDetailScreen(
                viewModel = hiltViewModel(),
                onOpenEpisodes = { podcastId ->
                    navController.navigate(podcastEpisodesRoute(podcastId))
                }
            )
        }
        composable(
            route = PODCAST_EPISODES_ROUTE + "/{$PODCAST_ID_ARG}",
            arguments = listOf(navArgument(PODCAST_ID_ARG) { type = NavType.StringType })
        ) {
            EpisodeListScreen(viewModel = hiltViewModel())
        }
        composable(AppDestinations.DOWNLOADS) { DownloadsScreen() }
        composable(AppDestinations.SETTINGS) { SettingsScreen() }
        composable(AppDestinations.PLAYER) { PlayerScreen(viewModel = playerViewModel) }
    }
}
