# Capability Spec: Navigation Skeleton

## What

建立 Jetpack Compose Navigation 基礎架構的能力規範，包含所有 route 定義、底部導覽列、以及各 feature 的 placeholder screen。

## Why

LeaRPcast 使用 Compose Navigation 作為全 App 導航系統。正確的 navigation 骨架讓所有後續 feature screen 都有明確的接入點。

## Canonical State

成功的 navigation 骨架必須滿足：

- `AppDestinations` 定義所有 routes（radio, podcast, downloads, settings, player）
- `AppNavHost` 組裝 NavHost 並接入各 feature screen
- `BottomNavItem` 使用 `labelResId: Int`（`R.string.nav_*`），不使用 hardcoded 字串
- 5 個 feature placeholder screens 均使用 `stringResource(R.string.placeholder_*)`

## UI 字串規則（強制）

所有字串必須來自 `strings.xml`：

| 用途 | Key |
|------|----|
| Bottom nav label | `R.string.nav_radio` 等 |
| Placeholder text | `R.string.placeholder_*_coming_soon` |

> ⚠︎ 禁止在任何 Composable 內使用 `Text("...")` 字串字面值。

## Feature Placeholder 路徑

| Feature | 精確路徑 |
|---------|---------|
| Radio | `feature/radio/src/main/.../feature/radio/ui/screen/RadioScreen.kt` |
| Podcast | `feature/podcast/src/main/.../feature/podcast/ui/screen/PodcastListScreen.kt` |
| Player | `feature/player/src/main/.../feature/player/ui/screen/PlayerScreen.kt` |
| Downloads | `feature/downloads/src/main/.../feature/downloads/ui/screen/DownloadsScreen.kt` |
| Settings | `feature/settings/src/main/.../feature/settings/ui/screen/SettingsScreen.kt` |

## Acceptance Check

- App starts with 4-tab bottom navigation bar
- All tabs navigate correctly without stack duplication
- No hardcoded strings in any Composable
- `R.string` lookup resolves for all nav labels

## Related Changes

- ISSUE-003: `issue-003-app-navigation-skeleton`
