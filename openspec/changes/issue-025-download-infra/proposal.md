## Why

Offline playback requires downloaded episode files. Media3's `DownloadManager` and `DownloadService` provide the infrastructure for queuing, downloading, and resuming downloads.

## What Changes

- Wire Media3 `DownloadManager` with a `SimpleCache` or `DatabaseProvider`.
- Create `PodcastDownloadService` extending Media3 `DownloadService`.
- Register `PodcastDownloadService` in `:core:media`'s `AndroidManifest.xml`.

## Capabilities

### New Capabilities
- `download-infra`: Media3 download infrastructure supporting queued and resumable episode downloads.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Unlocks ISSUE-026, ISSUE-027.
