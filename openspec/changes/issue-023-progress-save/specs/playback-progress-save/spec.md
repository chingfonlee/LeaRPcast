## ADDED Requirements

### Requirement: Periodic Progress Save
`PlaybackService` MUST save the current playback position to the DB at most every 30 seconds during active podcast playback.

#### Scenario: Playing for 30 seconds
- **WHEN** 30 seconds have passed during episode playback
- **THEN** `PlaybackProgressDao` is updated with the current position in milliseconds.

### Requirement: Immediate Save on Pause or Completion
`PlaybackService` MUST save progress immediately when playback is paused or an episode ends.

#### Scenario: User pauses playback
- **WHEN** user pauses an episode
- **THEN** progress is saved to DB immediately (within the same coroutine tick, not after the next 30s interval).
