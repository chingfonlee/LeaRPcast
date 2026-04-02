## MODIFIED Requirements

### Requirement: Podcast List Screen
`PodcastListScreen` MUST display subscribed podcasts as a management list ordered by the latest episode publish time from newest to oldest, and each item MUST show square artwork, the podcast title, and the latest episode title.

#### Scenario: Viewing the Podcast tab
- **WHEN** the user opens the Podcast tab
- **THEN** `PodcastListScreen` shows all subscribed podcasts in descending latest-episode order with summary content

#### Scenario: Podcast has no episodes
- **WHEN** a subscribed podcast has no episode records
- **THEN** the item shows a clear no-episode fallback state instead of a latest-episode title

### Requirement: Episode List Screen
`EpisodeListScreen` MUST continue to show all episodes of a selected podcast in chronological order.

#### Scenario: Viewing episodes
- **WHEN** the user taps a podcast in `PodcastListScreen`
- **THEN** navigation goes to `PodcastDetailScreen` then `EpisodeListScreen` showing episode titles and durations
