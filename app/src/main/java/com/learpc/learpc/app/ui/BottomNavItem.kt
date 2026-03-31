package com.learpc.learpc.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.learpc.learpc.app.R
import com.learpc.learpc.app.navigation.AppDestinations

enum class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
) {
    RADIO(AppDestinations.RADIO, R.string.nav_radio, Icons.Default.Radio),
    PODCAST(AppDestinations.PODCAST, R.string.nav_podcast, Icons.Default.Podcasts),
    DOWNLOADS(AppDestinations.DOWNLOADS, R.string.nav_downloads, Icons.Default.Download),
    SETTINGS(AppDestinations.SETTINGS, R.string.nav_settings, Icons.Default.Settings),
}
