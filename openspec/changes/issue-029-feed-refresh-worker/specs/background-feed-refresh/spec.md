## ADDED Requirements

### Requirement: Periodic Background Feed Refresh
`FeedRefreshWorker` MUST refresh all subscribed podcasts' RSS feeds on a recurring schedule.

#### Scenario: Worker runs on schedule
- **WHEN** WorkManager executes `FeedRefreshWorker`
- **THEN** `RefreshPodcastFeedUseCase` is called for each subscribed podcast, and new episodes are inserted.

### Requirement: Auto-Download After Refresh
After refresh, `FeedRefreshWorker` MUST evaluate and enqueue downloads for qualifying new episodes.

#### Scenario: New qualifying episode found
- **WHEN** a new episode is inserted during refresh and `EvaluateAutoDownloadUseCase` returns `true`
- **THEN** `EnqueueEpisodeDownloadUseCase` is called for that episode.
