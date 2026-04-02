## Context

RSS feeds are standard XML. We need an HTTP client to fetch them and an `XmlPullParser`-based parser to extract podcast and episode metadata without third-party XML libraries.

## Goals / Non-Goals

**Goals:** `PodcastFeedService` (OkHttp call returning raw XML string), `FeedParser` (XmlPullParser implementation), `RemoteFeedPodcast`/`RemoteFeedEpisode` DTOs.
**Non-Goals:** No DB writes here — data sources and use cases handle that.

## Decisions

- Use OkHttp directly instead of Retrofit for RSS fetch (Retrofit is for JSON APIs; RSS is raw XML).
- `FeedParser` is a pure function: `fun parse(xml: String): RemoteFeedPodcast`.

## Risks / Trade-offs

- **Risk**: Malformed RSS could throw `XmlPullParserException`.
  - **Mitigation**: Wrap parse call in `runCatching` and return `Result<RemoteFeedPodcast>`.
