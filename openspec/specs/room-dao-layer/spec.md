# room-dao-layer Specification

## Purpose
TBD - created by archiving change issue-010-dao-and-dto. Update Purpose after archive.
## Requirements
### Requirement: DAO CRUD and Query Operations
Each DAO MUST provide at minimum: `insert`, `update`/`upsert`, `delete`, and a `Flow`-returning query for common access patterns.

#### Scenario: Observing podcast list
- **WHEN** `PodcastDao.observeAll()` is collected
- **THEN** it emits the current list and re-emits on every DB change.

### Requirement: Relation DTO with Transaction
`PodcastWithEpisodes` MUST be a `@Relation`-annotated data class with an accompanying `@Transaction` DAO function.

#### Scenario: Loading podcast detail
- **WHEN** `PodcastDao.getPodcastWithEpisodes(podcastId)` is called
- **THEN** it returns the `PodcastEntity` together with its associated `EpisodeEntity` list in one atomic read.

