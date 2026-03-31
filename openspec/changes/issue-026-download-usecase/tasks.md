## 1. DefaultDownloadRepository

- [x] 1.1 Create `DefaultDownloadRepository` in repository layer injecting `DownloadRecordDao`.
- [x] 1.2 Implement `observeDownloads(): Flow<List<DownloadRecord>>` and `upsertRecord(record)`.
- [x] 1.3 Bind in `RepositoryModule`.

## 2. EnqueueEpisodeDownloadUseCase

- [x] 2.1 Create `EnqueueEpisodeDownloadUseCase` in `:domain` injecting `DownloadManager` and `DownloadRepository`.
- [x] 2.2 Check for existing non-failed record; return early if found.
- [x] 2.3 Build `DownloadRequest` from `episode.audioUrl` with `customData = episode.id`.
- [x] 2.4 Call `downloadManager.addDownload(request)` and upsert `DownloadRecordEntity(status = QUEUED)`.

## 3. Verification

- [x] 3.1 Run `./gradlew :domain:assembleDebug`.
- [ ] 3.2 Manually test: tap download on episode, check DB record is inserted.
