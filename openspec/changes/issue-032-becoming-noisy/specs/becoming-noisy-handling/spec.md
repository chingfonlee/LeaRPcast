## ADDED Requirements

### Requirement: Headphone Unplug Auto-Pause
The system MUST pause audio playback automatically when headphones are unplugged.

#### Scenario: Headphones removed during playback
- **WHEN** user removes wired headphones while audio is playing
- **THEN** playback pauses automatically and the state transitions to `PlaybackStateModel.Paused`.
