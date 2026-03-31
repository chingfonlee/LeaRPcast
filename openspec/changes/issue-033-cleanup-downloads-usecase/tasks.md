## 1. Use Case

- [x] 1.1 Create `CleanupDownloadsUseCase` in `:domain` injecting `EpisodeRepository`, `DownloadRepository`, `SettingsRepository`, `PlaybackController`.
- [x] 1.2 Implement: query completed downloads ??filter by retention settings and `isMarkedForKeep` ??exclude currently playing episode.
- [x] 1.3 For each candidate: delete `File(episode.localFilePath)`, update `EpisodeEntity.isDownloaded = false`, remove `DownloadRecordEntity`.

## 2. Unit Tests

- [x] 2.1 Test: currently playing episode is protected from deletion.
- [x] 2.2 Test: episode within retention period is not deleted.
- [x] 2.3 Test: `isMarkedForKeep = true` episode is not deleted.

## 3. Verification

- [x] 3.1 Run `./gradlew :domain:test`.
