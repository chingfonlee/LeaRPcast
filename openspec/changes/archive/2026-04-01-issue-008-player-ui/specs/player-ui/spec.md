## ADDED Requirements

### Requirement: Full Player Screen
`PlayerScreen` MUST display the currently playing item's title, subtitle, and an artwork placeholder, with a play/pause toggle button.

#### Scenario: User opens player
- **WHEN** user navigates to `AppDestinations.PLAYER`
- **THEN** `PlayerScreen` renders title/subtitle from `PlayerUiState.currentItem` and a play/pause button reflecting `PlayerUiState.isPlaying`.

### Requirement: Mini Player
A `MiniPlayer` composable MUST be visible at the bottom of the app when a media item is loaded, showing title and a play/pause button.

#### Scenario: Media begins loading
- **WHEN** `PlaybackController.currentItem` emits a non-null `PlayableItem`
- **THEN** `MiniPlayer` appears with the item title and a play/pause button.

### Requirement: No Hardcoded Strings
All visible text in `:feature:player` MUST use `stringResource()` referencing keys in `:feature:player/src/main/res/values/strings.xml`.

#### Scenario: String resource usage
- **WHEN** any composable in `:feature:player` displays text
- **THEN** it MUST call `stringResource(R.string.<key>)`, not hardcode a string literal.
