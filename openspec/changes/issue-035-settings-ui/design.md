## Context

Settings screen replaces the placeholder. Uses `SettingsViewModel` collecting `UserPreferences` from `DefaultSettingsRepository`.

## Goals / Non-Goals

**Goals:** `SettingsScreen` with working toggles; `SettingsViewModel` with update calls; settings persist across sessions.
**Non-Goals:** Theme customisation (future).

## Decisions

Use Material3 `Switch` components for boolean settings; `Slider` for playback speed.

## Risks / Trade-offs

- **Risk**: Settings changes don't apply retroactively to running `PlaybackService`.
  - **Mitigation**: For MVP, service reads settings at decision points (e.g., on focus gain); values naturally picked up from DataStore at that moment.
