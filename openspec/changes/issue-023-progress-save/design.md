## Context

Progress must persist across app restarts. `SavePlaybackProgressUseCase` is called by `PlaybackService`: throttled (every 15-30s) via `conflate()` / debounce flow, and immediately on pause/completion.

## Goals / Non-Goals

**Goals:** `SavePlaybackProgressUseCase`; integration into `PlaybackService` with throttle timer.
**Non-Goals:** UI for resume position display (that's ISSUE-024's ViewModel reading the stored position).

## Decisions

Use `tickerFlow` or `Flow.sample(interval)` in `PlaybackService` coroutine to emit position periodically. On pause/end events, call use case immediately with `emit-now` or direct suspend call.

## Risks / Trade-offs

- **Risk**: Progress save races with DB on rapid pause/play.
  - **Mitigation**: The `upsert` on `PlaybackProgressDao` is idempotent; latest value wins.
