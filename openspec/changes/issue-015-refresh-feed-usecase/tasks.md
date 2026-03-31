## 1. Use Case Implementation

- [x] 1.1 Create `RefreshPodcastFeedUseCase` in `:domain` injecting `PodcastRemoteDataSource` and `PodcastLocalDataSource`, `EpisodeLocalDataSource`.
- [x] 1.2 Implement `suspend operator fun invoke(podcastId: String, feedUrl: String): Result<Unit>`.
- [x] 1.3 Flow: `fetchFeed(feedUrl)` → `parse` → map episodes → upsert podcast metadata → upsert episodes (by GUID).
- [x] 1.4 Return `Result.failure(e)` on any network or parse error.

## 2. Domain Interface

- [x] 2.1 Expose `RefreshPodcastFeedUseCase` as a Hilt-injectable class (no separate interface needed for use cases).

## 3. Verification

- [x] 3.1 Write a unit test with mocked data sources verifying deduplication (same GUID not inserted twice).
- [x] 3.2 Run `./gradlew :domain:assembleDebug`.
