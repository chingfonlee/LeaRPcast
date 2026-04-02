## 1. Mappers

- [x] 1.1 Create `PodcastMapper.kt` with `PodcastEntity → Podcast` and `RemoteFeedPodcast → PodcastEntity`.
- [x] 1.2 Create `EpisodeMapper.kt` with `EpisodeEntity → Episode` and `RemoteFeedEpisode → EpisodeEntity`.

## 2. Local Data Sources

- [x] 2.1 Create `PodcastLocalDataSource` injecting `PodcastDao`.
- [x] 2.2 Create `EpisodeLocalDataSource` injecting `EpisodeDao`.
- [x] 2.3 Expose `observeAll()`, `getById()`, `upsert()`, `delete()` in each.

## 3. Remote Data Source

- [x] 3.1 Create `PodcastRemoteDataSource` injecting `PodcastFeedService` and `FeedParser`.
- [x] 3.2 Implement `suspend fun fetchFeed(url: String): Result<RemoteFeedPodcast>`.

## 4. Verification

- [x] 4.1 Run `./gradlew :core:database:assembleDebug :core:network:assembleDebug`.
