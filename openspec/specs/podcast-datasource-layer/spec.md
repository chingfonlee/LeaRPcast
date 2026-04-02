# podcast-datasource-layer Specification

## Purpose
TBD - created by archiving change issue-013-podcast-datasources. Update Purpose after archive.
## Requirements
### Requirement: Local Data Source Wraps DAO
`PodcastLocalDataSource` and `EpisodeLocalDataSource` MUST expose `Flow`-based observation and suspend mutation functions backed by their respective DAOs.

#### Scenario: Observing podcasts locally
- **WHEN** `PodcastLocalDataSource.observeAll()` is collected
- **THEN** it emits the same stream as `PodcastDao.observeAll()` mapped to domain models.

### Requirement: Remote Data Source Returns Domain Models
`PodcastRemoteDataSource.fetchFeed(url)` MUST return a `Result<RemoteFeedPodcast>` sourced from `PodcastFeedService` + `FeedParser`.

#### Scenario: Fetching remote feed
- **WHEN** `PodcastRemoteDataSource.fetchFeed(url)` is called
- **THEN** it returns parsed podcast and episode data, or a failure result on network/parse error.

