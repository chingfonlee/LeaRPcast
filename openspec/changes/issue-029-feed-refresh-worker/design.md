## Context

`FeedRefreshWorker` backgrounds the full podcast refresh + auto-download chain via WorkManager.

## Goals / Non-Goals

**Goals:** `FeedRefreshWorker` CoroutineWorker; periodic schedule in `LeaRPcastApplication`; chain: refresh → evaluate → enqueue.
**Non-Goals:** Error notification to user (future).

## Decisions

Period: 4h for MVP. Constraints: network required. Use `PeriodicWorkRequest`.

## Risks / Trade-offs

- **Risk**: WorkManager may delay work significantly on battery-optimised devices.
  - **Mitigation**: Accept as-is for MVP; users can manual-refresh via pull-to-refresh in ISSUE-016.
