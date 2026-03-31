# ISSUE-003 - Design: App Navigation Skeleton

## Navigation Architecture

建立 app-level Compose Navigation host 與主要 destinations，讓使用者可在主要頁面間切換，並為後續 feature 提供路由接入點。

## Route Definition

```kotlin
// app/src/main/java/com/learpc/learpc/app/navigation/AppDestinations.kt
object AppDestinations {
    const val RADIO = "radio"
    const val PODCAST = "podcast"
    const val DOWNLOADS = "downloads"
    const val SETTINGS = "settings"
    const val PLAYER = "player"  // full screen player (overlay)
}
```

## AppNavHost

```kotlin
// app/src/main/java/com/learpc/learpc/app/navigation/AppNavHost.kt
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

## Bottom Navigation Bar

| Tab | Route | Icon |
|-----|-------|------|
| Radio | `radio` | Icons.Default.Radio |
| Podcast | `podcast` | Icons.Default.Podcasts |
| Downloads | `downloads` | Icons.Default.Download |
| Settings | `settings` | Icons.Default.Settings |

## Placeholder Screens

所有 5 個 placeholder screen 都採相同模式，各自使用對應 feature module 的 `stringResource(...)`，不得 hardcode 字串。

```kotlin
@Composable
fun RadioScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.placeholder_radio_coming_soon))
    }
}
```

## strings.xml ownership

| Module | Keys | Purpose |
|--------|------|---------|
| `:app` | `app_name`, `nav_radio`, `nav_podcast`, `nav_downloads`, `nav_settings` | App shell and bottom navigation labels |
| `:feature:radio` | `placeholder_radio_coming_soon` | Radio placeholder screen |
| `:feature:podcast` | `placeholder_podcast_coming_soon` | Podcast placeholder screen |
| `:feature:player` | `placeholder_player_coming_soon` | Player placeholder screen |
| `:feature:downloads` | `placeholder_downloads_coming_soon` | Downloads placeholder screen |
| `:feature:settings` | `placeholder_settings_coming_soon` | Settings placeholder screen |

> `nav_*` keys live in `:app` because they are shell-level navigation labels. Placeholder strings live in the owning feature module so each screen can resolve its own resources without referencing `:app`.

## File Paths

```
app/src/main/java/com/learpc/learpc/app/
  navigation/
    AppDestinations.kt
    AppNavHost.kt
  ui/
    LeaRPcastApp.kt       // Scaffold + BottomNav + AppNavHost wiring
    BottomNavItem.kt      // tab metadata enum/data class
app/src/main/res/values/
  strings.xml             // nav_* + app_name

feature/radio/src/main/java/com/learpc/learpc/feature/radio/
  ui/screen/
    RadioScreen.kt        // placeholder uses feature/radio/src/main/res/values/strings.xml
feature/radio/src/main/res/values/
  strings.xml             // placeholder_radio_coming_soon

feature/podcast/src/main/java/com/learpc/learpc/feature/podcast/
  ui/screen/
    PodcastListScreen.kt  // placeholder uses feature/podcast/src/main/res/values/strings.xml
feature/podcast/src/main/res/values/
  strings.xml             // placeholder_podcast_coming_soon

feature/player/src/main/java/com/learpc/learpc/feature/player/
  ui/screen/
    PlayerScreen.kt       // placeholder uses feature/player/src/main/res/values/strings.xml
feature/player/src/main/res/values/
  strings.xml             // placeholder_player_coming_soon

feature/downloads/src/main/java/com/learpc/learpc/feature/downloads/
  ui/screen/
    DownloadsScreen.kt    // placeholder uses feature/downloads/src/main/res/values/strings.xml
feature/downloads/src/main/res/values/
  strings.xml             // placeholder_downloads_coming_soon

feature/settings/src/main/java/com/learpc/learpc/feature/settings/
  ui/screen/
    SettingsScreen.kt     // placeholder uses feature/settings/src/main/res/values/strings.xml
feature/settings/src/main/res/values/
  strings.xml             // placeholder_settings_coming_soon
```
