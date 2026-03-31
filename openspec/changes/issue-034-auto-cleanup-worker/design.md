## Context

`AutoCleanupWorker` backgrounds `CleanupDownloadsUseCase` via WorkManager, running after the feed refresh cycle.

## Goals / Non-Goals

**Goals:** `AutoCleanupWorker` CoroutineWorker; periodic schedule with daily interval.
**Non-Goals:** Notification to user (only log).

## Decisions

Period: daily. Constraint: not low battery. `ExistingPeriodicWorkPolicy.KEEP`.

## Risks / Trade-offs

- **Risk**: Cleanup run while currently playing may be tricky if cleanup runs between current item check and file delete.
  - **Mitigation**: `CleanupDownloadsUseCase` already guards this; worker just calls use case.
