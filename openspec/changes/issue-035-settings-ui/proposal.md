## Why

Users need to configure the app's behaviour: auto-download rules, Wi-Fi-only mode, resume-after-call preference, playback speed, and radio retry settings. The Settings tab already exists as a placeholder.

## What Changes

- Implement `SettingsScreen` with working toggles and preferences.
- Create `SettingsViewModel` reading/writing via `DefaultSettingsRepository`.

## Capabilities

### New Capabilities
- `settings-ui`: Functional settings screen connected to DataStore-backed preferences.

### Modified Capabilities
-

## Impact

- Module: `:feature:settings`
- String resources in `:feature:settings/src/main/res/values/strings.xml`.
