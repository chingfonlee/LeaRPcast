## Why

When a user plays an episode, we need to resolve whether to use the locally downloaded file or the remote URL for streaming. This encapsulated use case provides that decision to feature modules.

## What Changes

- Create `PlayEpisodeUseCase` in `:domain` checking local file existence first, then falling back to remote URL.

## Capabilities

### New Capabilities
- `play-episode-use-case`: Domain use case resolving local vs remote source and triggering episode playback.

### Modified Capabilities
-

## Impact

- Module: `:domain`
- Unlocks ISSUE-023 (progress save), ISSUE-024 (UI entry).
