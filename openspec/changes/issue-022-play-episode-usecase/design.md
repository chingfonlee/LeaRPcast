## Context

Episodes may be downloaded; use case checks local file existence first, falls back to remote URL.

## Goals / Non-Goals

**Goals:** `PlayEpisodeUseCase`, `EpisodePlayableMapper`.
**Non-Goals:** Download trigger (use `EnqueueEpisodeDownloadUseCase`).

## Decisions

`PlayEpisodeUseCase` calls `File(localPath).exists()` to check. Produces `PlayableItem` with appropriate URI.

## Risks / Trade-offs

- **Risk**: Local file partially downloaded but marked as complete.
  - **Mitigation**: DB `isDownloaded` flag is only set `true` by `DownloadService` completion callback; trust the flag.
