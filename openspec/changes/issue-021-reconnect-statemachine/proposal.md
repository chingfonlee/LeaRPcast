## Why

With the reconnect policy in place, we integrate it into `PlaybackService` so that when a radio stream drops, the service enters a `Reconnecting` state, attempts retries with backoff, and surfaces a `Failed` state to the UI if the max retry count is exceeded.

## What Changes

- Integrate `RadioReconnectPolicy` into `PlaybackService`'s error handling path.
- Add `Reconnecting` and `Failed` states to `PlaybackStateModel` (or verify they exist from ISSUE-005).

## Capabilities

### New Capabilities
- `radio-reconnect-state-machine`: Reconnect flow inside `PlaybackService` driven by `RadioReconnectPolicy`.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Completes the Phase 3 radio playback cycle.
