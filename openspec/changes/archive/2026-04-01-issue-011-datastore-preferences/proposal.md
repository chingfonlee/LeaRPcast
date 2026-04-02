## Why

User settings like auto-download rules, Wi-Fi-only mode, resume-after-call, and playback speed need to persist between sessions. DataStore (Proto or Preferences) is the modern Android replacement for SharedPreferences.

## What Changes

- Define `PreferenceKeys` for all settings described in Data Schema v1.
- Create `UserPreferences` data holder.
- Create `UserPreferencesDataSource` that reads/writes via DataStore.

## Capabilities

### New Capabilities
- `datastore-preferences`: Typed preference read/write backed by Jetpack DataStore.

### Modified Capabilities
-

## Impact

- Module: `:core:datastore`
- Unlocks ISSUE-011 → ISSUE-014 (Settings repository), ISSUE-028, ISSUE-030.
