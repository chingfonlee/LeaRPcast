## 1. Network Dependencies

- [x] 1.1 Verify `okhttp` in `:core:network/build.gradle.kts`.

## 2. Remote DTOs

- [x] 2.1 Create `RemoteFeedPodcast` and `RemoteFeedEpisode` data classes in `:core:network`.

## 3. PodcastFeedService

- [x] 3.1 Create `PodcastFeedService` with `suspend fun fetchFeed(url: String): Result<String>` using OkHttp.
- [x] 3.2 Provide `PodcastFeedService` via Hilt in `NetworkModule`.

## 4. FeedParser

- [x] 4.1 Create `FeedParser` with `fun parse(xml: String): Result<RemoteFeedPodcast>` using `XmlPullParser`.
- [x] 4.2 Extract: `<channel>` title, description, `<itunes:image>`, and `<item>` guid, title, `<enclosure url>`, `<itunes:duration>`, pubDate.

## 5. Verification

- [x] 5.1 Write a unit test with a sample RSS XML string and assert parsed fields.
