## ADDED Requirements

### Requirement: Podcast List Screen
`PodcastListScreen` MUST display the list of subscribed podcasts from the local DB.

#### Scenario: Viewing podcast list
- **WHEN** user taps the Podcast tab
- **THEN** `PodcastListScreen` shows all subscribed podcasts with title and artwork placeholder.

### Requirement: Episode List Screen
`EpisodeListScreen` MUST show all episodes of a selected podcast in chronological order.

#### Scenario: Viewing episodes
- **WHEN** user taps a podcast in `PodcastListScreen`
- **THEN** navigation goes to `PodcastDetailScreen` then `EpisodeListScreen` showing episode titles and durations.
