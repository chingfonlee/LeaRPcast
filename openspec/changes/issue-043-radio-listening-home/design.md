## Context

Radio already has discovery and management-related behavior, but the user problem for this change is different: fixed listeners want a page that behaves like a listening hub, not a station admin tool. The user should be able to open Radio, see their saved stations immediately, tap one, and hear something right away.

This change stays inside the existing module structure and should reuse the current playback and station data flows. The main work is in the Radio feature UI and its state model, not in a new backend or storage architecture.

Relevant paths:
- `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt`
- `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/viewmodel/RadioViewModel.kt`
- `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/model/RadioUiState.kt`
- `feature/radio/src/main/res/values/strings.xml`

## Goals / Non-Goals

**Goals:**
- Make the Radio page feel like a listening-first destination.
- Put saved stations in front of the user immediately.
- Let the user start playback with one clear tap.
- Keep adding a favorite station available, but secondary.
- Support the required page states without hiding the user's current context.
- Keep the interaction model simple enough for mobile use and low learning cost.

**Non-Goals:**
- Replacing the existing station management experience from `issue-040-radio-station-management`.
- Adding cloud sync, account-backed favorites, or cross-device state.
- Introducing a new station storage model or a new playback engine.
- Designing advanced scheduling, queueing, or recommendation features on this page.

## Decisions

1. Use a compact list-first layout instead of a large card-based layout.
   - Why: Fixed listeners scan station names faster in lists, and compact rows support one-tap playback better than large visual tiles.
   - Alternatives considered: large cards, carousel rows, and a grid. Those would look heavier and slow down the primary task.

2. Make the station row itself a playback target.
   - Why: The main action is "play this station now," so the row tap should behave as the fast path.
   - Alternatives considered: requiring a separate play button tap, or opening a detail page first. Both add friction and are worse for regular listening.

3. Keep favorite management as a secondary icon action.
   - Why: The user goal is listening, not editing. Favorite actions need to be visible enough to understand, but quieter than playback.
   - Alternatives considered: hiding favorites behind a long-press or overflow menu. That would reduce discoverability for a common secondary action.

4. Use a visible now-playing banner above the list.
   - Why: Users need immediate confirmation of what is playing, especially when they quickly switch between stations.
   - Alternatives considered: relying only on the row highlight. That is not enough on a mobile screen with scrolling content.

5. Represent page behavior through a single UI state model.
   - Why: The page has distinct loading, empty, playing, success, playback failure, and network failure states, and those should be explicit instead of inferred from scattered booleans.
   - Alternatives considered: multiple independent flags. That tends to create edge cases and makes the screen harder to reason about.

6. Keep add-favorite as a lightweight bottom sheet or inline panel.
   - Why: The secondary action should be close to the page but should not take users away from the listening context.
   - Alternatives considered: full-screen navigation for adding a station. That is better for complex management, but too heavy for a quick favorite flow.

## Risks / Trade-offs

- [Risk] A compact list could feel too plain if it is not visually separated from the now-playing area.
  - [Mitigation] Use a clear listening header and strong state labels so the page feels intentional rather than empty.
- [Risk] Making the whole row tappable can conflict with secondary favorite controls.
  - [Mitigation] Keep the favorite icon visually distinct and reserve the row tap for playback only.
- [Risk] The page may blur into the management experience if too many edit controls appear.
  - [Mitigation] Keep edit and reorder actions out of the primary home surface unless they are explicitly needed there.
- [Risk] Error states can interrupt the listening flow if they are too aggressive.
  - [Mitigation] Preserve the last successful list and show recoverable errors inline instead of replacing the page with a blocking screen.

## Migration Plan

- No schema migration is expected.
- The page should reuse the existing radio station and playback flows.
- If the view model currently exposes a broader management state, this change should narrow the home surface to the state needed for listening-first behavior without changing the underlying storage contract.

## Open Questions

- Should the empty state recommend a few starter stations, or only provide an add action?
- Should the add favorite action open a bottom sheet or a dedicated full-screen route?
- Should the currently playing station be pinned at the top of the list or only shown in the banner?
