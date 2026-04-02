## ADDED Requirements

### Requirement: Subscribed Podcast Management List
`PodcastListScreen` MUST display subscribed podcasts as a management list ordered by the latest episode publish time from newest to oldest.

#### Scenario: Viewing the Podcast tab
- **WHEN** the user opens the Podcast tab
- **THEN** `PodcastListScreen` shows all subscribed podcasts in descending latest-episode order

### Requirement: Podcast List Item Summary
Each podcast item MUST show a square artwork area, the podcast title, and the latest episode title.

#### Scenario: Viewing a populated item
- **WHEN** a subscribed podcast has at least one episode
- **THEN** the item displays the podcast artwork, the podcast title, and the latest episode title

### Requirement: No-Episode Fallback
The system MUST show a clear fallback presentation for subscribed podcasts that do not have any episodes yet.

#### Scenario: Podcast has no episodes
- **WHEN** a subscribed podcast has no episode records
- **THEN** the item shows a placeholder artwork state and copy indicating that no episodes are available yet

### Requirement: Podcast Item Actions
`PodcastListScreen` MUST provide a way to open podcast details and unsubscribe from a podcast.

#### Scenario: User opens a podcast
- **WHEN** the user taps a podcast item
- **THEN** the app navigates to `PodcastDetailScreen` for that podcast

#### Scenario: User unsubscribes from a podcast
- **WHEN** the user chooses unsubscribe for a podcast and confirms the action
- **THEN** the podcast is removed from the subscribed list

### Requirement: List State Handling
`PodcastListScreen` MUST present loading, empty, and error states for the management list.

#### Scenario: Data is loading
- **WHEN** the list has not finished loading
- **THEN** the screen shows skeleton loading content instead of an empty list

#### Scenario: No subscriptions exist
- **WHEN** there are no subscribed podcasts
- **THEN** the screen shows an empty state with a call to add the first podcast

#### Scenario: Loading fails
- **WHEN** the list cannot be loaded
- **THEN** the screen shows an error state with a retry action
