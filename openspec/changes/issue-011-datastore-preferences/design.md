## Context

DataStore Preferences provides type-safe, async settings storage. All user preferences in Data Schema v1 must map to typed keys and be exposed via a `Flow`.

## Goals / Non-Goals

**Goals:** `PreferenceKeys`, `UserPreferences` data holder, `UserPreferencesDataSource` with `observePreferences(): Flow<UserPreferences>` and individual update functions.
**Non-Goals:** UI for settings (ISSUE-035).

## Decisions

Use `Preferences DataStore` (not Proto) for simplicity. Keys prefixed with their category.

## Risks / Trade-offs

- **Risk**: Type mismatch between saved Int and expected Enum.
  - **Mitigation**: Map Enum ↔ Int in `UserPreferences` data class constructor, not in the DataStore key.
