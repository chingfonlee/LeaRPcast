# refresh-feed-use-case Specification

## Purpose
TBD - created by archiving change issue-015-refresh-feed-usecase. Update Purpose after archive.
## Requirements
### Requirement: Feed Refresh Updates DB
`RefreshPodcastFeedUseCase` MUST fetch, parse, and upsert podcast and episode data into the local DB without producing duplicate episodes.

#### Scenario: Refreshing a feed for the first time
- **WHEN** `invoke(podcastId, feedUrl)` is called for a new podcast
- **THEN** the podcast metadata and all episodes are inserted into the DB.

#### Scenario: Refreshing an existing feed
- **WHEN** `invoke(podcastId, feedUrl)` is called for an existing podcast with new episodes in the feed
- **THEN** only new episodes (by GUID) are inserted; existing episodes are updated if metadata changed.

