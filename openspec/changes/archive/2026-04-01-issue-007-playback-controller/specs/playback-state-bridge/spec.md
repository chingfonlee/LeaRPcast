## ADDED Requirements

### Requirement: Reactive Playback State
The system MUST expose playback state as a `StateFlow<PlaybackStateModel>` so UI components can collect changes reactively without directly touching ExoPlayer.

#### Scenario: State changes from idle to playing
- **WHEN** `ExoPlayer` transitions to `STATE_READY` and `playWhenReady = true`
- **THEN** `PlaybackController.playbackState` emits `PlaybackStateModel.Playing`.

### Requirement: Domain-level Commands
`PlaybackController` MUST expose `play()`, `pause()`, and `seekTo(positionMs: Long)` as the only way for ViewModels to command the player.

#### Scenario: ViewModel pauses playback
- **WHEN** a ViewModel calls `playbackController.pause()`
- **THEN** `MediaController.pause()` is invoked and state transitions to `PlaybackStateModel.Paused`.

### Requirement: Current Item Observable
`PlaybackController` MUST expose `currentItem: StateFlow<PlayableItem?>` reflecting the currently playing media item.

#### Scenario: Track changes
- **WHEN** the player loads a new `MediaItem`
- **THEN** `currentItem` emits a `PlayableItem` with the corresponding title, subtitle, and mediaUri.
