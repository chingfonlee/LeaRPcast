## Context

`PlaybackService` currently has no error recovery. We add a `Player.Listener` that detects `STATE_IDLE` caused by errors, checks `PlaybackErrorClassifier`, and starts a retry coroutine with `RadioReconnectPolicy` delay if retryable.

## Goals / Non-Goals

**Goals:** Reconnect coroutine in `PlaybackService`; `Reconnecting` + `Failed` state propagated via `PlaybackController`; retry counter reset when new item is set.
**Non-Goals:** No UI reconnecting indicator (add to Player UI in later polish).

## Decisions

Use `CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)` tied to service lifecycle.

## Risks / Trade-offs

- **Risk**: Re-preparing the `MediaItem` restarts buffering; could cause duplicate history entries.
  - **Mitigation**: Only re-prepare; do not re-add to queue.
