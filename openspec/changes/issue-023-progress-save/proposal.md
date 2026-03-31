## Why

Podcast listeners need to resume where they left off. This issue implements periodic (throttled) progress saves and immediate saves on pause and completion.

## What Changes

- Create `SavePlaybackProgressUseCase` in `:domain`.
- Integrate into `PlaybackService`: throttle at 15–30s for periodic saves, immediate on pause and track end.

## Capabilities

### New Capabilities
- `playback-progress-save`: Resume-from-position capability via throttled and event-triggered progress persistence.

### Modified Capabilities
-

## Impact

- Modules: `:domain`, `:core:media`
- Unlocks ISSUE-024 (users see resume position), ISSUE-038 (integration test).
