## Why

With `PlaybackController` providing reactive state (ISSUE-007), we can now build the user-facing Player UI. This is the first user-visible deliverable in Phase 1: a full-screen player and a collapsible mini-player that shows what's currently playing and lets the user play/pause.

## What Changes

- Create `PlayerScreen` composable (full-screen player).
- Create `MiniPlayer` composable (persistent bottom bar).
- Create `PlayerViewModel` that collects from `PlaybackController`.
- Define `PlayerUiState` and player-related UI events.

## Capabilities

### New Capabilities
- `player-ui`: Full-screen and mini player UI surfaces connected to `PlaybackController`.

### Modified Capabilities
-

## Impact

- Impacted Modules: `:feature:player`, `:app` (to embed `MiniPlayer` in `LeaRPcastApp` scaffold)
- String keys for player UI MUST be added to `:feature:player/src/main/res/values/strings.xml`.
