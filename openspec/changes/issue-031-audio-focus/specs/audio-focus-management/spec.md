## ADDED Requirements

### Requirement: Audio Focus Handled Automatically
`PlayerFactory` MUST configure ExoPlayer to handle audio focus via `setHandleAudioBecomingNoisy(true)` and `AudioAttributes` with `usage = USAGE_MEDIA` and `handleAudioFocus = true`.

#### Scenario: Audio focus taken by another app
- **WHEN** another app takes audio focus
- **THEN** ExoPlayer automatically pauses or ducks without additional service code.

### Requirement: Policy-Driven Resume on Focus Gain
When audio focus is regained, `PlaybackService` MUST consult `ResumeAfterInterruptionPolicy` before resuming.

#### Scenario: Focus regained after brief call
- **WHEN** audio focus is regained after a brief call and `shouldResume = true`
- **THEN** `PlaybackService` resumes playback and clears the stored `InterruptionSnapshot`.
