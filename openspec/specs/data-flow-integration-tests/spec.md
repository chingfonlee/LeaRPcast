# data-flow-integration-tests Specification

## Purpose
TBD - created by archiving change issue-038-integration-tests. Update Purpose after archive.
## Requirements
### Requirement: Feed Refresh Integration Test
A test MUST verify that `RefreshPodcastFeedUseCase` inserts episodes into Room when given a mock remote feed.

#### Scenario: Feed refresh integration
- **WHEN** `RefreshPodcastFeedUseCase` is called with a mock `RemoteFeedPodcast` containing 3 episodes
- **THEN** `EpisodeDao.observeByPodcastId(podcastId)` emits a list of 3 episodes from the in-memory DB.

### Requirement: Progress Save/Reload Integration Test
A test MUST verify that saved playback progress is retrievable from the DB.

#### Scenario: Progress persistence round-trip
- **WHEN** `SavePlaybackProgressUseCase` saves position 45000ms for an episode
- **THEN** `PlaybackProgressDao.getByEpisodeId(episodeId)` returns `positionMs = 45000`.

