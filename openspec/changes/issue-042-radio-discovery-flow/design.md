## Context

`issue-040` is now focused on radio station management and personal library organization. This change defines the complementary experience: helping users discover what to listen to, try it quickly, and save it without friction.

The key idea is that users should not need to know a station URL or even a station name before they can start listening. Discovery should feel light, optional, and welcoming.

## Goals / Non-Goals

**Goals:**
- Make Radio discovery-first when the user wants to add something new.
- Reduce friction for users who do not know a station name or stream URL.
- Offer recommendations, search, and manual entry as clear, low-pressure paths.
- Let users preview a station before saving it.
- Use broad locale, language, or region hints when available, without requiring precise location access.

**Non-Goals:**
- Replace the existing radio management page from `issue-040`.
- Add cloud sync or account-based personalization.
- Build a full recommendation engine with cross-device tracking.
- Add scheduled playback controls as part of the MVP discovery flow.

## UX Principles

1. Discovery before data entry.
   - Start from "what would you like to hear?" instead of "please enter station details."

2. Preview before save.
   - The user should be able to listen first and save later if the station feels right.

3. Recommendations should help, not block.
   - Locale, language, and region hints can improve starting points, but they must never become a permission gate.

4. Manual entry stays available.
   - Users who already know the station name and stream URL should have a direct path, but it should not be the default burden.

## Proposed Flow

### Quick Add Entry Point
- Radio should expose a simple entry point for discovery.
- The entry point should feel like an invitation to listen, not a form to complete.

### Discovery Surface
- The first discovery screen should offer a small set of clear choices:
  - recommended stations,
  - search by station name or keyword,
  - manual advanced entry for known station name and stream URL.
- Recommended stations should be presented as ready-to-play options.

### Preview Surface
- Tapping a recommended or searched station should begin playback immediately.
- A lightweight save action should appear once the user has heard enough to decide.
- The distinction between "play once" and "save for later" should be obvious.

### Recommendation Bias
- Use broad locale, language, or region signals when available to bias recommendation order.
- Prefer safe defaults like app language or broad region before asking for precise location.
- If location is unavailable, the discovery flow must still function normally.

### Future Listening Utilities
- Scheduled start and scheduled stop are useful, but they belong after the core discovery flow.
- They should be treated as follow-up listening utilities, not a requirement for saving a station.

## Risks / Trade-offs

- [Risk] The discovery flow could become too complex if too many recommendation inputs are exposed.
  - [Mitigation] Keep the first screen small and present only a few clear paths.
- [Risk] Users may not understand the difference between previewing a station and saving it.
  - [Mitigation] Make save an explicit post-preview action with clear copy.
- [Risk] Location-based suggestions could feel intrusive.
  - [Mitigation] Make them optional and approximate, with locale or language as the default.

## Migration Plan

- No schema migration is expected for the MVP discovery flow.
- Discovery can build on existing radio station metadata and repository patterns.
- If more advanced recommendations are added later, they can be layered on without changing the listening-first entry flow.

## Open Questions

- Should recommended stations appear as a short curated list or a ranked feed?
- Should the user see search before recommendations, or recommendations before search?
- Should the save action appear directly in the preview player or in a separate confirmation step?
