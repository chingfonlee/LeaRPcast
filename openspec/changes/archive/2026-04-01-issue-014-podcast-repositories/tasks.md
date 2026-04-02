## 1. Repository Layer Setup

- [x] 1.1 Create `DefaultPodcastRepository` implementing `PodcastRepository` (domain interface) in `:app/data/repository` (per Project Structure Spec v1).
- [x] 1.2 Create `DefaultEpisodeRepository` implementing `EpisodeRepository`.
- [x] 1.3 Create `DefaultSettingsRepository` implementing `SettingsRepository`.

## 2. Repository Logic

- [x] 2.1 `DefaultPodcastRepository.observePodcasts()` delegates to `PodcastLocalDataSource.observeAll()`.
- [x] 2.2 `DefaultEpisodeRepository.observeByPodcastId(id)` delegates to `EpisodeLocalDataSource`.
- [x] 2.3 `DefaultSettingsRepository` delegates all operations to `UserPreferencesDataSource`.

## 3. Hilt Binding

- [x] 3.1 Update `RepositoryModule` to bind all three repositories to their domain interfaces.

## 4. Verification

- [x] 4.1 Run `./gradlew :app:kspDebugKotlin` — Hilt graph resolves all repository bindings.
