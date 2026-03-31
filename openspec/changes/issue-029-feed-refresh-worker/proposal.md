## Why

Feed refreshing must happen in the background on a schedule, independent of the app's foreground lifecycle. `FeedRefreshWorker` uses WorkManager to orchestrate: refresh feed → evaluate auto-download → enqueue downloads.

## What Changes

- Create `FeedRefreshWorker` using WorkManager.
- Schedule periodic work in `LeaRPcastApplication`.
- Chain: `RefreshPodcastFeedUseCase` → `EvaluateAutoDownloadUseCase` → `EnqueueEpisodeDownloadUseCase`.

## Capabilities

### New Capabilities
- `background-feed-refresh`: Periodically refresh podcast feeds and auto-enqueue qualifying new episodes for download.

### Modified Capabilities
-

## Impact

- Module: `:app/work`
- Unlocks ISSUE-038 (integration test).
