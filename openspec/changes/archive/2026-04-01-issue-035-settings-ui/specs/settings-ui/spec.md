## ADDED Requirements

### Requirement: Settings Persist via DataStore
Changes made in `SettingsScreen` MUST be persisted to DataStore and reflected in subsequent `observePreferences()` emissions.

#### Scenario: Toggling Wi-Fi-only
- **WHEN** user toggles the Wi-Fi-only switch
- **THEN** `SettingsRepository.setWifiOnlyDownload(value)` is called and the toggle reflects the new state after recomposition.

### Requirement: All Settings Displayed
`SettingsScreen` MUST display all settings from `UserPreferences`: Wi-Fi-only, auto-delete mode, resume-after-call, playback speed, radio retry settings.

#### Scenario: Opening settings
- **WHEN** user navigates to the Settings tab
- **THEN** all preference toggles and sliders are visible and correctly reflect current DataStore values.
