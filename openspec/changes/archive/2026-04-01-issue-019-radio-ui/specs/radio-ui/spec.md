## ADDED Requirements

### Requirement: Radio Station List Display
`RadioScreen` MUST display a scrollable list of radio stations with name and country.

#### Scenario: Stations loaded
- **WHEN** `RadioUiState` contains a non-empty station list
- **THEN** each station is shown in a `LazyColumn` row with name, country, and a play button.

### Requirement: Tap to Play
Tapping a station MUST invoke `PlayRadioUseCase` and start playback.

#### Scenario: Tapping a station
- **WHEN** user taps a station row
- **THEN** `RadioViewModel.playStation(station)` is called, invoking `PlayRadioUseCase`, and `MiniPlayer` becomes visible.
