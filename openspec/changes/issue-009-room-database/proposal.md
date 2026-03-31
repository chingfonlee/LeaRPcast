## Why

All Podcast and Radio data must persist across sessions. Following the Data Schema v1, we need a Room database with all required entities. This is a foundational step before any data layer or UI work can proceed.

## What Changes

- Create `AppDatabase` with Room annotations.
- Create all entity classes: `PodcastEntity`, `EpisodeEntity`, `SubscriptionEntity`, `PlaybackProgressEntity`, `DownloadRecordEntity`, `RadioStationEntity`.

## Capabilities

### New Capabilities
- `room-database-schema`: All entity classes and `AppDatabase` conforming to Data Schema v1.

### Modified Capabilities
-

## Impact

- Module: `:core:database`
- Unlocks ISSUE-010 (DAO), ISSUE-013 (data sources), and all repository work.
