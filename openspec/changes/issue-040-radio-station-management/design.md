## Context

The Radio feature already has a working browse-and-play flow. `RadioViewModel` only exposes station playback, while `DefaultRadioRepository.observeStations()` streams stations from the local Room cache and refreshes from RadioBrowser in the background. The data model already includes `sortOrder`, and the DAO orders stations by favorite state, sort order, and name, which gives us an existing foundation for user-controlled ordering.

What is missing is the user-facing management layer: the Radio tab cannot add custom stations, edit or delete them, or change their order. The goal is to expose those capabilities without disturbing playback, cache-first loading, or RadioBrowser refresh behavior.

## Goals / Non-Goals

**Goals:**
- Let users add custom radio stations with a name and stream URL.
- Let users edit or delete stations in their radio library.
- Let users reorder stations and keep that ordering across app restarts.
- Preserve the current play flow and cache-first refresh behavior.
- Keep the feature within the existing module structure.

**Non-Goals:**
- Cloud sync or account-backed station libraries.
- Folders, playlists, or multi-level station organization.
- Changes to the playback engine or `PlaybackService`.
- RadioBrowser catalog moderation or remote station editing.

## Decisions

1. Use the existing `radio_station` table and `sort_order` column as the source of truth for ordering.
   - The current schema already stores order, favorite state, and timestamps.
   - This avoids a new table, keeps the change local, and does not require a schema migration.
   - Alternative considered: a separate custom-station table. Rejected because it would duplicate the model and complicate refresh logic.

2. Add dedicated domain use cases for station management instead of letting the UI call the repository directly.
   - Likely files:
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/AddRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/UpdateRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/DeleteRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/ReorderRadioStationsUseCase.kt`
   - This keeps `feature:radio` thin and preserves the existing architecture rule that business logic lives in the domain layer.
   - Alternative considered: expose CRUD/reorder calls directly from `RadioRepository` into the ViewModel. Rejected because it couples the UI to storage details.

3. Keep RadioBrowser refresh behavior, but preserve local ordering and custom entries.
   - `DefaultRadioRepository` already copies `sortOrder`, `isFavorite`, and timestamps from the existing row when remote data is refreshed.
   - Custom stations should use stable local IDs so refresh never collides with user-created rows.
   - Alternative considered: disable refresh for managed stations. Rejected because it would degrade the existing cache refresh experience.

4. Implement management UI inside the Radio feature instead of creating a new feature module.
   - The add/edit entry points, reorder affordances, and validation live in `feature/radio`.
   - The current `RadioScreen` is the right place to surface the management flow, since the capability is part of the radio library itself.
   - Likely files:
     - `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt`
     - `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/viewmodel/RadioViewModel.kt`
     - `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/model/RadioUiState.kt`

5. Use a modal add/edit surface and explicit reorder controls.
   - A bottom sheet or dialog is enough for entering station name and stream URL.
   - Reorder can be implemented with drag handles or move up/down controls, depending on Compose support and testability.
   - Alternative considered: a full-screen editor. Rejected because it adds navigation overhead for a task that should stay lightweight.

## Risks / Trade-offs

- [Risk] Reordering and refresh may compete for the same rows. → [Mitigation] Centralize all sort-order writes in the domain layer and keep repository refresh behavior order-preserving.
- [Risk] Custom station URLs may be invalid or unplayable. → [Mitigation] Validate required fields before save and surface clear error messages in the add/edit flow.
- [Risk] Manual edits to synced RadioBrowser stations could be overwritten on the next refresh. → [Mitigation] Keep the first version focused on custom stations plus ordering, and decide later whether synced stations are editable.
- [Risk] Drag-and-drop list reordering can be fiddly in Compose. → [Mitigation] Fall back to explicit move controls if gesture handling becomes too expensive or brittle.

## Migration Plan

- No database migration is expected because the existing radio schema already includes `sort_order`.
- Add the new use cases and UI state first, then wire the Radio screen to them.
- Verify that the merged radio list still loads from cache first and that custom stations survive a refresh cycle.
- Rollback is straightforward: remove the new UI entry points and use cases while leaving the existing repository and playback flow intact.

## Open Questions

- Should editing apply only to custom stations, or also to RadioBrowser-synced stations?
- Should reordering apply to the full radio list, or only to user-created stations?
- Should favorites always stay above ordered stations, or should user order take priority over favorite sorting?
