## ADDED Requirements

### Requirement: Download Service Lifecycle
`PodcastDownloadService` MUST run as a foreground service during active downloads, showing a notification.

#### Scenario: Download enqueued
- **WHEN** a download is added to `DownloadManager`
- **THEN** `PodcastDownloadService` starts in the foreground with a notification showing download progress.

### Requirement: Download InfraStructure DI Provision
`DownloadManager` MUST be provided by Hilt as a singleton accessible to use cases.

#### Scenario: DI resolve DownloadManager
- **WHEN** `EnqueueEpisodeDownloadUseCase` is injected
- **THEN** it receives the same `DownloadManager` singleton as `PodcastDownloadService`.
