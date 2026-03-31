## Context

`EnqueueEpisodeDownloadUseCase` adds download requests to `DownloadManager` and syncs state to Room via `DefaultDownloadRepository`.

## Goals / Non-Goals

**Goals:** `EnqueueEpisodeDownloadUseCase`; `DefaultDownloadRepository`; `DownloadRecordEntity` upsert.
**Non-Goals:** Download progress polling UI (ISSUE-027).

## Decisions

Use Media3 `DownloadManager.addDownload(DownloadRequest)`. Track with `DownloadRecordEntity.status`.

## Risks / Trade-offs

- **Risk**: Duplicate download requests for same episode.
  - **Mitigation**: Check if `DownloadRecordEntity` with `episodeId` already has a non-failed status before enqueue.
