## 1. Dependencies

- [x] 1.1 Add `media3-exoplayer` (already present), `media3-datasource-okhttp` to `:core:media` if not present.

## 2. Download Manager

- [x] 2.1 Create `DownloadManagerProvider` in `:core:media` providing `DownloadManager` with `ExoDatabaseProvider` + `SimpleCache`.
- [x] 2.2 Add to `MediaModule`.

## 3. PodcastDownloadService

- [x] 3.1 Create `PodcastDownloadService` extending Media3 `DownloadService`.
- [x] 3.2 Override `getDownloadManager()` to return the injected `DownloadManager`.

## 4. Manifest Registration

- [x] 4.1 Register `PodcastDownloadService` in `:core:media/AndroidManifest.xml` with `foregroundServiceType="dataSync"`.
- [x] 4.2 Add `FOREGROUND_SERVICE_DATA_SYNC` permission.

## 5. Verification

- [x] 5.1 Run `./gradlew :core:media:assembleDebug` — no Hilt graph errors.
