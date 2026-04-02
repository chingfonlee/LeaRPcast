# play-radio-use-case Specification

## Purpose
TBD - created by archiving change issue-018-play-radio-usecase. Update Purpose after archive.
## Requirements
### Requirement: Play Radio Use Case Maps Station to PlayableItem
`PlayRadioUseCase` MUST convert a `RadioStation` to a `PlayableItem` and command `PlaybackController` to play it.

#### Scenario: Playing a radio station
- **WHEN** `invoke(station)` is called
- **THEN** `PlaybackController` receives a `PlayableItem` with the station's stream URL and name, and begins playback.

