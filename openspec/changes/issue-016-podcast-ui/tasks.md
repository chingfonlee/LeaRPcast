## 1. UiState Classes

- [x] 1.1 Create `PodcastUiState` (loading, list, error) in `:feature:podcast`.
- [x] 1.2 Create `EpisodeUiState` (loading, list, error).

## 2. ViewModels

- [x] 2.1 Create `PodcastViewModel` with `@HiltViewModel`, injecting `PodcastRepository` and `RefreshPodcastFeedUseCase`.
- [x] 2.2 Create `PodcastDetailViewModel` with `@HiltViewModel`, injecting `EpisodeRepository`.

## 3. Screens

- [x] 3.1 Create `PodcastListScreen.kt` with `LazyColumn` of podcast items.
- [x] 3.2 Create `PodcastDetailScreen.kt` showing detail and episode list entry.
- [x] 3.3 Create `EpisodeListScreen.kt` with `LazyColumn` of episode rows.

## 4. Navigation

- [x] 4.1 Add podcast detail and episode list routes to `AppNavHost` in `:app`.
- [x] 4.2 Pass `podcastId` as nav argument.

## 5. Strings

- [x] 5.1 Add UI strings to `:feature:podcast/src/main/res/values/strings.xml`.

## 6. Verification

- [x] 6.1 Run `./gradlew :feature:podcast:assembleDebug`.
- [ ] 6.2 Launch app; Podcast tab shows list (may be empty if no subscriptions added yet).
