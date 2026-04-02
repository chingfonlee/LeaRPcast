## Context

`MediaController` speaks ExoPlayer. ViewModels should speak domain. This layer converts the two. The `PlayerEventMapper` is also a key testability seam — policy tests can supply fake `Player.State` instead of needing a running ExoPlayer.

## Goals / Non-Goals

**Goals:**
- Create `PlaybackController` interface in `:core:media` with `StateFlow` properties and command methods.
- Implement `DefaultPlaybackController` that receives an injected `MediaController` and translates events.
- Provide `PlayerEventMapper` that maps `Player.STATE_*` → `PlaybackStateModel` variants.

**Non-Goals:**
- No queue management yet.
- No seek-bar position polling (ISSUE-008 can do its own position updates via a `ticker`).

## Decisions

- `DefaultPlaybackController` listens via `MediaController.addListener(Player.Listener)`.
- `StateFlow` is backed by `MutableStateFlow` with an initial value of `PlaybackStateModel.Idle`.
- All public methods (`play`, `pause`, `seekTo`) delegate directly to `MediaController` methods.

## Risks / Trade-offs

- **Risk**: `MediaController` may not be ready immediately (it's a `Future`).
  - **Mitigation**: `DefaultPlaybackController` should guard against null controller; initially emits `Idle` until connection is established.
