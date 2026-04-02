## Context

The Radio feature already has a working browse-and-play flow, but the current experience still reads more like a station directory than a listening hub. From a user perspective, the real job-to-be-done is: "I want to hear something I like now, and I want it to be easy to save that station when I find it."

This design reorients the Radio tab around listening first and management second. The user should immediately understand:

- what can be played right now,
- how to add a station they want to keep,
- and how to keep preferred stations easy to reach later.

The existing Room schema already provides a foundation for ordering and persistence, so the design should build on that instead of introducing a new storage model or a heavier navigation flow.

## Goals / Non-Goals

**Goals:**
- Make the Radio tab feel like a listening-first hub.
- Put an obvious add-station path in the user's line of sight.
- Let users save stations they personally want to hear and keep them easy to access later.
- Preserve the current play flow and cache-first refresh behavior.
- Keep station management lightweight, fast, and low-friction.

**Non-Goals:**
- Cloud sync or account-backed station libraries.
- Folders, playlists, or multi-level station organization.
- Changes to the playback engine or `PlaybackService`.
- RadioBrowser catalog moderation or remote station editing.

## UX Principles

1. Listen first, manage second.
   - The page should surface playback and quick access before CRUD controls.
   - Management actions should never feel more important than starting playback.

2. Add is part of listening.
   - The add-station flow should feel like a natural extension of discovering a station.
   - If a user hears something they like, they should be able to save it immediately without navigating away.

3. Preferred stations should feel effortless.
   - The UI should help users keep "the stations I actually want to hear" near the top.
   - Reordering is a convenience that reduces friction the next time the user opens Radio.

4. Management must stay out of the way until needed.
   - Edit, delete, and reorder actions should be present, but secondary.
   - A user who just wants to listen should not feel like they are entering an admin screen.

## Design Decisions

1. Keep the existing `radio_station` table and `sort_order` column as the source of truth.
   - This keeps the feature local and avoids a schema migration.
   - `sort_order` should remain the mechanism for preserving a user's preferred listening order.

2. Use dedicated domain use cases for station management.
   - Likely files:
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/AddRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/UpdateRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/DeleteRadioStationUseCase.kt`
     - `domain/src/main/java/com/learpc/learpc/domain/usecase/ReorderRadioStationsUseCase.kt`
   - This keeps the radio UI thin and keeps the listening experience decoupled from storage logic.

3. Preserve RadioBrowser refresh, but make personal stations durable.
   - Refresh should continue to keep the library fresh.
   - User-added stations and user-defined ordering should survive refresh cycles.
   - The user's saved listening choices should not feel fragile.

4. Structure the UI around "play now" and "save for later."
   - The top of the screen should prioritize a listen-now surface, not a raw list.
   - The list below should help the user pick from their stations quickly.
   - Management controls should be available, but visually quieter than play actions.

5. Keep add/edit lightweight and immediate.
   - A dialog or bottom sheet is the right level of interaction for saving a station.
   - Users should be able to add a station with minimal typing and minimal cognitive load.
   - If the user already knows the stream URL, the flow should feel quick and direct.

6. Use explicit reorder affordances.
   - Up/down controls are easier to understand than hidden gestures for this use case.
   - The goal is not advanced list editing; the goal is "make my favorite station easier to tap next time."

## Proposed UX Shape

### Listening-Focused Header
- Show a prominent listening entry area near the top.
- If something is playing, show the current station and a clear play/pause state.
- If nothing is playing, show a friendly prompt that makes the next action obvious, such as starting a station or adding one the user likes.

### Quick Add Entry Point
- Keep an always-visible add action near the top of the screen.
- The copy should emphasize personal choice, e.g. "Add a station you want to hear."
- The add flow should feel like the user is saving a listening preference, not filling out a form.

### Discovery-First Quick Add
- If the user does not already know a station name or stream URL, the app should reduce friction by starting from listening choices instead of data entry.
- The first screen of the add flow should offer a small number of clear paths:
  - recommended stations based on locale, language, or broad region,
  - search by station name or keyword,
  - and a manual advanced entry path for users who already know the stream URL.
- Recommendations should feel like a helpful starting point, not a mandatory profile setup step.
- If location is used at all, it should be approximate and optional, with a preference for locale, language, or region defaults before asking for precise GPS access.

### Preview Before Save
- Users should be able to tap a station, start playback immediately, and then save it if they like it.
- Saving should happen after discovery and preview, so the flow feels like "I found something good and want to keep it."
- The UI should make the difference between "play once" and "save for later" obvious, but keep both actions one tap away.
- A station that is already playing can surface a lightweight save action in the same context.

### Helpful Defaults for Unknown Stations
- When the user does not know what to choose, the app should still give them something pleasant to start with.
- Suggested defaults can include:
  - the most popular stations for the user's broad region,
  - stations in the current app language,
  - or a short curated list of easy-to-try stations.
- The goal is to let the user hear something quickly, then decide whether it is worth saving.

### Primary Station List
- Stations should be listed in the order that best supports listening.
- The list should make it easy to tap play first, then optionally manage the station.
- Rows should visually suggest "this is something to listen to now", not just "this is a record in a database."

### Secondary Management Actions
- Edit and delete should remain available, but secondary to playback.
- Reorder should exist for convenience, not as the dominant interaction.
- A long-press or overflow menu can be used if it keeps the default view calmer.

### Future Listening Utilities
- Time-based features such as scheduled start or scheduled stop can be valuable, but they should be treated as follow-up listening utilities rather than the core add flow.
- These features are better suited for later enhancement once the main quick-add and playback flow is already effortless.
- They should not block the simpler experience of discovering a station, previewing it, and saving it.

## Risks / Trade-offs

- [Risk] If reorder is too prominent, the screen starts to feel like an admin tool instead of a listening hub.
  - [Mitigation] Keep play actions dominant and move management into secondary affordances.
- [Risk] If the add flow is too minimal, it may not feel welcoming for users discovering stations on the fly.
  - [Mitigation] Use friendly copy and fast entry patterns, not a dense form.
- [Risk] If custom stations and RadioBrowser stations are treated identically, the user may not understand what they personally control.
  - [Mitigation] Visually distinguish "saved by me" from "browsed from source" where needed.
- [Risk] Reordering can still be fiddly if the list gets crowded.
  - [Mitigation] Prefer explicit controls over complex drag gestures for MVP.

## Migration Plan

- No database migration is expected because the existing radio schema already includes `sort_order`.
- The UI can be refocused without changing the underlying data model.
- If any follow-up work is needed, it should mostly be in how the screen presents listening versus management, not in how stations are stored.

## Open Questions

- Should the first visible radio section be "Now Playing" or "Quick Add" when nothing is playing?
- Should the list visually separate "my saved stations" from browsed stations?
- Should management controls be hidden behind overflow actions by default, or shown inline but de-emphasized?
