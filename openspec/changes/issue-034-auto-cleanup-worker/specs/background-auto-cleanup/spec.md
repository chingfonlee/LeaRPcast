## ADDED Requirements

### Requirement: Daily Automatic Cleanup
`AutoCleanupWorker` MUST run `CleanupDownloadsUseCase` at most once per day.

#### Scenario: Worker runs
- **WHEN** WorkManager executes `AutoCleanupWorker`
- **THEN** `CleanupDownloadsUseCase` is called and eligible downloads are deleted.
