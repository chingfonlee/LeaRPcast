## ADDED Requirements

### Requirement: Automatic Reconnect on Retryable Error
`PlaybackService` MUST automatically retry radio playback after a retryable error, with delay from `RadioReconnectPolicy`.

#### Scenario: Transient network drop
- **WHEN** ExoPlayer emits a retryable error during radio playback
- **THEN** `PlaybackService` waits for the policy delay, then calls `player.prepare()` again; `PlaybackStateModel.Reconnecting` is emitted.

### Requirement: Failed State on Max Retries
After exhausting retries, `PlaybackService` MUST emit `PlaybackStateModel.Error` and cease reconnection.

#### Scenario: Max retries exceeded
- **WHEN** reconnect attempts reach `RadioReconnectPolicy.maxAttempts`
- **THEN** `PlaybackController.playbackState` emits `PlaybackStateModel.Error` and no further retry is attempted.
