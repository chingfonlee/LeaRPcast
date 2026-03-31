## Why

Android requires explicit audio focus management. We integrate with ExoPlayer's built-in audio focus handling and layer our `ResumeAfterInterruptionPolicy` on top to handle transient focus loss correctly.

## What Changes

- Verify ExoPlayer `setHandleAudioBecomingNoisy(true)` and `setAudioAttributes` with `handleAudioFocus = true` are set in `PlayerFactory`.
- Add `Player.Listener.onPlaybackStateChanged` / focus callbacks to save/restore `InterruptionSnapshot`.
- Apply `ResumeAfterInterruptionPolicy` on focus gain to conditionally resume.

## Capabilities

### New Capabilities
- `audio-focus-management`: Proper request, loss, and regain of audio focus with policy-driven auto-resume.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Unlocks ISSUE-032, ISSUE-036 (QA), ISSUE-037 (tests).
