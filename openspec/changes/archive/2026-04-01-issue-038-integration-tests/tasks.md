## 1. Test Setup

- [x] 1.1 Create `IntegrationTest` base class in `:core:testing` with `Rule` for in-memory `AppDatabase`.
- [x] 1.2 Add `kotlinx-coroutines-test` to test dependencies.

## 2. Feed Refresh Integration Test

- [x] 2.1 Create `RefreshPodcastFeedUseCaseIntegrationTest`.
- [x] 2.2 Mock `PodcastRemoteDataSource` to return a known `RemoteFeedPodcast` with 3 episodes.
- [x] 2.3 Call use case ??query `EpisodeDao` ??assert 3 episodes inserted.
- [x] 2.4 Call again (no new episodes) ??assert no duplicates (still 3).

## 3. Progress Save/Reload Test

- [x] 3.1 Create `SavePlaybackProgressUseCaseIntegrationTest`.
- [x] 3.2 Call use case with position 45000ms ??query `PlaybackProgressDao` ??assert correct value returned.

## 4. Verification

- [x] 4.1 Run `./gradlew :core:testing:test` (or relevant module) ??all integration tests pass.
