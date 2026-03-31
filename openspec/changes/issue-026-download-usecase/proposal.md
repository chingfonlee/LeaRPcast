## Why

`DownloadManager` alone doesn't update our Room DB. `EnqueueEpisodeDownloadUseCase` bridges the gap: it requests a download from Media3 and updates `DownloadRecordEntity` and episode cache fields in the DB.

## What Changes

- Create `EnqueueEpisodeDownloadUseCase` in `:domain`.
- Create `DefaultDownloadRepository` tracking download state in Room.

## Capabilities

### New Capabilities
- `episode-download-management`: Enqueue download requests and synchronise DB download state.

### Modified Capabilities
-

## Impact

- Modules: `:domain`, repository layer
- Unlocks ISSUE-027 (UI), ISSUE-028 (auto-download), ISSUE-033 (cleanup).
