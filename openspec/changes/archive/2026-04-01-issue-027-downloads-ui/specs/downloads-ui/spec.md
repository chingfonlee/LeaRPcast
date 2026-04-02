## ADDED Requirements

### Requirement: Downloads List Screen
`DownloadsScreen` MUST show all download records with their current status.

#### Scenario: Viewing downloads
- **WHEN** user taps the Downloads tab
- **THEN** `DownloadsScreen` shows a list of episode titles with status labels (Queued/Downloading/Completed).

### Requirement: Cancel Download Action
Each downloading/queued item MUST have a cancel button.

#### Scenario: Cancelling a download
- **WHEN** user taps cancel on a queued/downloading item
- **THEN** `DownloadManager.removeDownload(contentId)` is called and the record is removed from the list.
