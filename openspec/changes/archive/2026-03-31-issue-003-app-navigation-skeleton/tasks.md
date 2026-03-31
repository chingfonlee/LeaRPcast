# ISSUE-003 - Tasks: App Navigation Skeleton

## Overview

建立 Compose Navigation 骨架，讓主要頁面可切換，並為後續 feature 提供路由接入點。

---

## Task 1: 為 `:app` build.gradle.kts 加入 Navigation 相關依賴

**Effort**: S  
**File**: `app/build.gradle.kts`

```kotlin
dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
}
```

**Acceptance Criteria**:
- [x] Navigation Compose 依賴已加入

---

## Task 2: 建立 AppDestinations

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/navigation/AppDestinations.kt`

```kotlin
package com.learpc.learpc.app.navigation

object AppDestinations {
    const val RADIO = "radio"
    const val PODCAST = "podcast"
    const val DOWNLOADS = "downloads"
    const val SETTINGS = "settings"
    const val PLAYER = "player"
}
```

**Acceptance Criteria**:
- [x] 所有 route constants 建立完成

---

## Task 3: 建立 BottomNavItem

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/ui/BottomNavItem.kt`

```kotlin
package com.learpc.learpc.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
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
```

**Acceptance Criteria**:
- [x] 4 個 tab metadata 建立完成
- [x] label 使用 `R.string.nav_*`，不 hardcode

---

## Task 4: 建立 Placeholder Screens 與 feature-local 字串

**Effort**: S  

各 feature module 建立空白 placeholder composable，並在各自的 `src/main/res/values/strings.xml` 中持有對應的 `coming_soon` 字串。screen 只使用 `stringResource(...)`，不 hardcode。

```kotlin
// feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt
@Composable
fun RadioScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_radio_coming_soon))
    }
}

// feature/podcast/src/main/java/com/learpc/learpc/feature/podcast/ui/screen/PodcastListScreen.kt
@Composable
fun PodcastListScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_podcast_coming_soon))
    }
}

// feature/player/src/main/java/com/learpc/learpc/feature/player/ui/screen/PlayerScreen.kt
@Composable
fun PlayerScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_player_coming_soon))
    }
}

// feature/downloads/src/main/java/com/learpc/learpc/feature/downloads/ui/screen/DownloadsScreen.kt
@Composable
fun DownloadsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_downloads_coming_soon))
    }
}

// feature/settings/src/main/java/com/learpc/learpc/feature/settings/ui/screen/SettingsScreen.kt
@Composable
fun SettingsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_settings_coming_soon))
    }
}
```

**Acceptance Criteria**:
- [x] 5 個 placeholder screen 建立完成
- [x] 所有 screen 使用 `stringResource(R.string.placeholder_*)`
- [x] 每個 feature module 都有自己的 `src/main/res/values/strings.xml`
- [x] feature module 可被 `:app` 正確引用

---

## Task 5: 建立 AppNavHost

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/navigation/AppNavHost.kt`

```kotlin
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
```

**Acceptance Criteria**:
- [x] AppNavHost 已建立
- [x] 4 個 tab 可正確切換
- [x] back stack 行為符合預期

---

## Task 6: 建立 LeaRPcastApp 與 MainActivity 接線

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/ui/LeaRPcastApp.kt`

```kotlin
@Composable
fun LeaRPcastApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomNavItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
    ) { innerPadding ->
        AppNavHost(navController, Modifier.padding(innerPadding))
    }
}
```

**Acceptance Criteria**:
- [x] App shell 可成功顯示 bottom navigation
- [x] 4 個 tab 可以切換
- [x] `MainActivity.setContent` 已改成呼叫 `LeaRPcastApp()`

---

## Handoff Summary

```
[Handoff Summary]
Completed Issue: ISSUE-003

Changed Files:
- app/build.gradle.kts (MODIFIED - added Navigation, Compose deps)
- app/src/main/java/.../app/navigation/AppDestinations.kt (NEW)
- app/src/main/java/.../app/navigation/AppNavHost.kt (NEW)
- app/src/main/java/.../app/ui/BottomNavItem.kt (NEW)
- app/src/main/java/.../app/ui/LeaRPcastApp.kt (NEW)
- app/src/main/java/.../app/MainActivity.kt (MODIFIED - setContent updated)
- feature/radio/.../ui/screen/RadioScreen.kt (NEW - placeholder)
- feature/podcast/.../ui/screen/PodcastListScreen.kt (NEW - placeholder)
- feature/player/.../ui/screen/PlayerScreen.kt (NEW - placeholder)
- feature/downloads/.../ui/screen/DownloadsScreen.kt (NEW - placeholder)
- feature/settings/.../ui/screen/SettingsScreen.kt (NEW - placeholder)

What Is Ready For Next Step:
- Full navigation skeleton working
- 4-tab bottom nav bar functional
- All feature placeholder screens accessible
- Foundation for all Phase 1 work

Known Gaps:
- No theme applied (Material3 default only)
- Player is placeholder only (ISSUE-008)
- All feature screens are empty stubs

Recommended Next Issue: ISSUE-004 (core:media skeleton)
Note: Phase 0 completes here and the project moves into Phase 1 (ISSUE-004 ~ ISSUE-008)
```

