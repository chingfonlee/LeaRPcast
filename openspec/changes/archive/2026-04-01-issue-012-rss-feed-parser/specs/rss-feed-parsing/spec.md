## ADDED Requirements

### Requirement: RSS Feed Fetch
The system MUST fetch a raw RSS XML string from a given URL using OkHttp.

#### Scenario: Fetching a valid RSS feed
- **WHEN** `PodcastFeedService.fetchFeed(url)` is called with a valid RSS URL
- **THEN** it returns the raw XML string of the feed.

### Requirement: RSS Feed Parsing
The system MUST parse a raw RSS XML string into a `RemoteFeedPodcast` containing podcast metadata and a list of `RemoteFeedEpisode` items.

#### Scenario: Parsing standard RSS
- **WHEN** `FeedParser.parse(xml)` is called with valid RSS 2.0 XML
- **THEN** it returns a `RemoteFeedPodcast` with title, description, artwork URL, and at least one episode with guid, title, audio URL, and duration.
