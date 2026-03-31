## Why

Podcast content is distributed via RSS feeds. We need to fetch and parse these feeds to add podcasts to the app. This issue creates the HTTP fetch + XML parse pipeline as the foundation for all Podcast data ingestion.

## What Changes

- Add `PodcastFeedService` (Retrofit or OkHttp-based) to fetch raw RSS XML.
- Create `FeedParser` using `XmlPullParser` to extract podcast/episode fields.
- Define intermediate `RemoteFeedPodcast` and `RemoteFeedEpisode` DTOs.

## Capabilities

### New Capabilities
- `rss-feed-parsing`: Fetch and parse RSS XML into intermediate domain models.

### Modified Capabilities
-

## Impact

- Module: `:core:network`
- Unlocks ISSUE-013, ISSUE-015.
