# download-cleanup-policy Specification

## Purpose
TBD - created by archiving change issue-033-cleanup-downloads-usecase. Update Purpose after archive.
## Requirements
### Requirement: Safe Candidate Selection
`CleanupDownloadsUseCase` MUST NOT select the currently playing episode as a cleanup candidate.

#### Scenario: Currently playing episode is completed
- **WHEN** a completed episode is also the `currentItem` in `PlaybackController`
- **THEN** it MUST NOT be selected for deletion.

### Requirement: File and DB Atomicity
Deletion MUST update the DB record to `isDownloaded = false` in the same operation as deleting the file.

#### Scenario: Successful cleanup
- **WHEN** cleanup is run and a candidate episode's file is deleted
- **THEN** `EpisodeEntity.isDownloaded` is set to `false` and `DownloadRecordEntity` is removed.

