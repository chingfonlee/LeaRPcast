## 1. ViewModel

- [x] 1.1 Create `DownloadsViewModel` with `@HiltViewModel`, injecting `DownloadRepository` and `DownloadManager`.
- [x] 1.2 Expose `downloadsState: StateFlow<DownloadsUiState>` from `observeDownloads()`.
- [x] 1.3 Add `cancelDownload(contentId)` calling `downloadManager.removeDownload(contentId)`.

## 2. Screen

- [x] 2.1 Create `DownloadsScreen.kt` with `LazyColumn` of download rows.
- [x] 2.2 Each row: title, status label, cancel `IconButton` (visible on QUEUED/DOWNLOADING).

## 3. Strings

- [x] 3.1 Add string keys to `:feature:downloads/src/main/res/values/strings.xml`.

## 4. Verification

- [x] 4.1 Run `./gradlew :feature:downloads:assembleDebug`.
