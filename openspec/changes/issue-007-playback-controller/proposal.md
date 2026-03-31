## Why

Once `PlaybackService` is wired (ISSUE-006), the UI still has no way to observe playback state in a reactive, Kotlin-idiomatic way. This issue creates the bridge: a `PlaybackController` that listens to `MediaController` events, maps them to our domain `PlaybackStateModel`, and exposes a `StateFlow` for ViewModels to collect.

## What Changes

- Create `PlaybackController` interface and `DefaultPlaybackController` implementation.
- Create `PlayerEventMapper` to convert `Player.State` → `PlaybackStateModel`.
- Expose `playbackState: StateFlow<PlaybackStateModel>`, `currentItem: StateFlow<PlayableItem?>`, and basic commands (play, pause, seek).

## Capabilities

### New Capabilities
- `playback-state-bridge`: A reactive `StateFlow`-based bridge between `MediaController` and the UI layer, using domain models only.

### Modified Capabilities
-

## Impact

- Impacted Modules: `:core:media`
- Unlocks ISSUE-008 (Player UI can now observe state).
