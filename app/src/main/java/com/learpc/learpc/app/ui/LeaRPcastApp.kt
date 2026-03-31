package com.learpc.learpc.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.learpc.learpc.app.navigation.AppNavHost
import com.learpc.learpc.app.navigation.AppDestinations
import com.learpc.learpc.feature.player.ui.component.MiniPlayer
import com.learpc.learpc.feature.player.ui.viewmodel.PlayerViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun LeaRPcastApp() {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerUiState by playerViewModel.uiState.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            Column {
                if (playerUiState.currentItem != null) {
                    MiniPlayer(
                        uiState = playerUiState,
                        onClick = {
                            navController.navigate(AppDestinations.PLAYER) {
                                launchSingleTop = true
                            }
                        },
                        onPlayPauseClick = playerViewModel::playOrPause
                    )
                }

                NavigationBar {
                    BottomNavItem.entries.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                            label = { Text(stringResource(item.labelResId)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            playerViewModel = playerViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
