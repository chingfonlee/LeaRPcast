## Context

Downloads tab needs real content: a list of episodes with download status badges and a cancel button.

## Goals / Non-Goals

**Goals:** `DownloadsScreen`, `DownloadsViewModel`, status display (QUEUED/DOWNLOADING/COMPLETED).
**Non-Goals:** Retry failed download (future).

## Decisions

`DownloadsViewModel` observes `DownloadRepository.observeDownloads()`. Cancel calls `DownloadManager.removeDownload(contentId)`.

## Risks / Trade-offs

- **Risk**: Progress percentage requires polling `DownloadManager.currentDownloads`.
  - **Mitigation**: For MVP, show QUEUED/DOWNLOADING/COMPLETED labels without percentage; add progress in polish phase.
