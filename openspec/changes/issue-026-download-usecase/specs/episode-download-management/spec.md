## ADDED Requirements

### Requirement: Download Request Deduplication
`EnqueueEpisodeDownloadUseCase` MUST NOT enqueue a duplicate download if the episode already has a queued or downloading record.

#### Scenario: Duplicate enqueue attempt
- **WHEN** `invoke(episode)` is called for an episode already in the download queue
- **THEN** no new `DownloadRequest` is submitted and the use case returns without error.

### Requirement: DB State Update on Enqueue
When a download is successfully enqueued, `DownloadRecordEntity` MUST be upserted with `status = QUEUED`.

#### Scenario: Successful enqueue
- **WHEN** `invoke(episode)` enqueues a new download
- **THEN** `DownloadRecordDao.upsert` is called with a record containing `status = QUEUED`.
