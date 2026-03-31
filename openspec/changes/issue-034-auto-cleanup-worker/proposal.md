## Why

`CleanupDownloadsUseCase` defines the rules but doesn't schedule execution. `AutoCleanupWorker` runs it in the background on a periodic schedule via WorkManager.

## What Changes

- Create `AutoCleanupWorker` invoking `CleanupDownloadsUseCase`.
- Schedule periodic cleanup in `LeaRPcastApplication` alongside `FeedRefreshWorker`.

## Capabilities

### New Capabilities
- `background-auto-cleanup`: Scheduled background cleanup of stale episode downloads.

### Modified Capabilities
-

## Impact

- Module: `:app/work`
- Completes Phase 5 download lifecycle.
