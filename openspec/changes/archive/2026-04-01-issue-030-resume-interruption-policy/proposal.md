## Why

Calls and other audio focus events interrupt playback. We need a structured policy that, given an `InterruptionSnapshot` and user settings, decides whether to auto-resume after the interruption ends.

## What Changes

- Create `ResumeAfterInterruptionPolicy` evaluating snapshot timestamp, interruption duration, user settings, and whether the user manually paused.

## Capabilities

### New Capabilities
- `resume-interruption-policy`: Pure decision function for auto-resume after audio focus events.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Unlocks ISSUE-031 (audio focus integration), ISSUE-037 (unit tests).
