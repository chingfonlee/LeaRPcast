## Why

Feature modules must never access DAOs or the RSS parser directly. This issue creates the proper abstraction layer: local data sources wrapping DAOs, and a remote data source wrapping the RSS feed service.

## What Changes

- Create `PodcastLocalDataSource`, `EpisodeLocalDataSource` wrapping DAOs.
- Create `PodcastRemoteDataSource` wrapping `PodcastFeedService` and `FeedParser`.
- Create entity↔domain mappers.

## Capabilities

### New Capabilities
- `podcast-datasource-layer`: Clean data source abstraction preventing direct DAO/parser access from feature modules.

### Modified Capabilities
-

## Impact

- Module: `:core:database`, `:core:network`
- Unlocks ISSUE-014.
