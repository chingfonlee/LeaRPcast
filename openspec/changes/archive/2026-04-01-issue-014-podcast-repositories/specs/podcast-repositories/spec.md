## ADDED Requirements

### Requirement: Local-First Repository
`DefaultPodcastRepository.observePodcasts()` MUST return a `Flow` sourced from the local DB, not the network.

#### Scenario: Repository emits local data
- **WHEN** `observePodcasts()` is collected
- **THEN** it emits from `PodcastLocalDataSource`, updating whenever the DB changes.

### Requirement: Settings Repository Delegation
`DefaultSettingsRepository` MUST delegate all reads and writes to `UserPreferencesDataSource`.

#### Scenario: Reading settings via repository
- **WHEN** `SettingsRepository.observeSettings()` is collected
- **THEN** it emits the same stream as `UserPreferencesDataSource.observePreferences()`.
