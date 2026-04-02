## Context

Media3 `DownloadManager` requires a `DatabaseProvider` (Room or custom) and a `Cache` instance. `PodcastDownloadService` extends Media3 `DownloadService`, runs in the foreground, and coordinates with `DownloadManager`.

## Goals / Non-Goals

**Goals:** `PodcastDownloadService` extending `DownloadService`; `DownloadManager` wired with `DatabaseProvider`; manifest registration; DI provision.
**Non-Goals:** Download UI (ISSUE-027); enqueue use case (ISSUE-026).

## Decisions

Use `StandaloneDownloadService` + `MediaStoreWriteAccessInterceptor`-free path: store files in app-private storage via `Context.getExternalFilesDir(null)` or `filesDir`.

## Risks / Trade-offs

- **Risk**: `DownloadService` must be a foreground service; easy to miss on Android 14+.
  - **Mitigation**: Declare `foregroundServiceType="dataSync"` in manifest.
