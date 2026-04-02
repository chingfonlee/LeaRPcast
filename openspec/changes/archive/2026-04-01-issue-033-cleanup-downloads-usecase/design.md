## Context

Stale downloads accumulate. `CleanupDownloadsUseCase` selects candidates based on played status and retention rules, deletes files, and updates the DB.

## Goals / Non-Goals

**Goals:** `CleanupDownloadsUseCase` with pure candidate selection logic + file delete + DB update.
**Non-Goals:** Background scheduling (ISSUE-034).

## Decisions

Cleanup candidates: `episode.isCompleted AND NOT episode.isMarkedForKeep AND downloadedAt < (now - retentionDays * 86400000)`. Safe to delete: NOT currently playing (check vs `PlaybackController.currentItem`).

## Risks / Trade-offs

- **Risk**: Deleting file while it's being played (e.g., if cleanup runs during playback).
  - **Mitigation**: Compare `CleanupDownloadsUseCase.invoke()` with `PlaybackController.currentItem?.mediaId`; skip if match.
