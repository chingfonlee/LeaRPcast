## Why

Feature modules need a single domain entry point to start playing a radio station without directly manipulating `PlaybackService` or `ExoPlayer`. `PlayRadioUseCase` maps a `RadioStation` to a `PlayableItem` and passes it to `PlaybackController`.

## What Changes

- Create `PlayRadioUseCase` in `:domain`.
- Create `RadioPlayableMapper` converting station data to `PlayableItem`.

## Capabilities

### New Capabilities
- `play-radio-use-case`: Domain entry point for radio playback, mapping station to `PlayableItem` and triggering playback controller.

### Modified Capabilities
-

## Impact

- Module: `:domain`
- Unlocks ISSUE-019, ISSUE-021.
