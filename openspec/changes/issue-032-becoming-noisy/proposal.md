## Why

When a user unplugs headphones, audio should pause automatically to prevent unexpected loud playback from speakers. Android broadcasts `ACTION_AUDIO_BECOMING_NOISY` for this purpose.

## What Changes

- Ensure ExoPlayer's `setHandleAudioBecomingNoisy(true)` is active (done in `PlayerFactory`; verify here).
- Register a `BroadcastReceiver` as a fallback if needed.
- Test that removing headphones pauses playback on device/emulator.

## Capabilities

### New Capabilities
- `becoming-noisy-handling`: Automatic pause on headphone unplug via becoming-noisy event.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Part of standard audio hygiene; completes Phase 5.
