## ADDED Requirements

### Requirement: Episode Play Button in List
Each episode row in `EpisodeListScreen` MUST have a play button that invokes `PlayEpisodeUseCase`.

#### Scenario: Tapping episode play button
- **WHEN** user taps play on an episode row
- **THEN** `PlayEpisodeUseCase` is called and `MiniPlayer` shows the episode.

### Requirement: Currently Playing Highlight
The episode row that is currently playing MUST be visually highlighted (e.g., different icon or row colour).

#### Scenario: Active episode highlight
- **WHEN** `PlaybackController.currentItem.mediaId` matches an episode's ID
- **THEN** that episode row shows a "currently playing" visual indicator.
