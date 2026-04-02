## Context

First visible UI milestone. `PlayerScreen` shows the current `PlayableItem` from `PlaybackController.currentItem` and renders play/pause based on `playbackState`. `MiniPlayer` is always visible at the bottom of the `Scaffold` when something is playing.

## Goals / Non-Goals

**Goals:**
- `PlayerScreen`: artwork placeholder, title, subtitle, play/pause button, progress bar stub.
- `MiniPlayer`: compact row with title, play/pause; tappable to open full `PlayerScreen`.
- `PlayerViewModel`: collects `PlaybackController` flows; exposes `PlayerUiState`.
- All visible strings via `stringResource()` from `:feature:player`'s `strings.xml`.

**Non-Goals:**
- No playback speed control (Phase 6).
- No queue/next/prev (future issue).
- No artwork loading network (placeholder only).

## Decisions

- `MiniPlayer` is placed inside `LeaRPcastApp`'s `Scaffold` `bottomBar` slot, stacked above `NavigationBar`.
- `PlayerScreen` is a separate composable destination in `AppNavHost` (`AppDestinations.PLAYER`).

## Risks / Trade-offs

- **Risk**: `MiniPlayer` animation stacking with `BottomNav` may require custom layout.
  - **Mitigation**: Use a `Column` inside `bottomBar` to stack them vertically.
