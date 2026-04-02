# datastore-preferences Specification

## Purpose
TBD - created by archiving change issue-011-datastore-preferences. Update Purpose after archive.
## Requirements
### Requirement: Typed Preference Keys
All preference keys MUST be defined in `PreferenceKeys` as `Preferences.Key<T>` constants, matching Data Schema v1 settings fields.

#### Scenario: Reading a preference
- **WHEN** `UserPreferencesDataSource.observePreferences()` is collected
- **THEN** it emits a `UserPreferences` object with values from DataStore, with defaults applied if keys are absent.

### Requirement: Preference Updates
`UserPreferencesDataSource` MUST provide individual suspend update functions for each setting.

#### Scenario: Updating Wi-Fi-only setting
- **WHEN** `setWifiOnlyDownload(true)` is called
- **THEN** DataStore persists the value and the next emission of `observePreferences()` reflects the change.

