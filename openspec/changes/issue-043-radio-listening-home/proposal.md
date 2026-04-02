## Why

The current Radio experience is too close to a station management surface and does not match the main job of fixed radio listeners: find a familiar station quickly and start playing it with as little friction as possible. A dedicated listening home is needed so users can open Radio and immediately reach their favorite stations, with adding a new favorite kept secondary and lightweight.

## What Changes

- Add a dedicated Radio listening home that prioritizes already saved or configured stations on first load.
- Make station playback the primary action on the page, with tap-to-play behavior from the station list.
- Add a secondary entry point for adding a favorite station without forcing users into a management-first flow.
- Show a clear playing state, loading state, empty state, playback failure state, and network failure state on the same surface.
- Optimize the page for mobile usage and one-handed tapping by keeping the primary list compact and easy to scan.
- Keep all interactive elements defined and purposeful; do not introduce decorative controls without behavior.

## Capabilities

### New Capabilities
- `radio-listening-home`: listening-first radio page behavior, quick playback, favorite-first ordering, empty-state guidance, and mobile-friendly interaction states.

### Modified Capabilities
- 

## Impact

- `feature/radio` needs a listening-first home screen composition, state handling, and interaction wiring for play, favorite, empty, and error states.
- `feature/radio/src/main/res/values/strings.xml` will need strings for page title, station actions, empty state copy, playback feedback, and error recovery.
- `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/` will need UI state and screen updates to support a compact station list and a visible now-playing area.
- `:domain` and `:app/data/repository` should remain compatible with existing station playback and saved-station data; this change should reuse current data flows rather than introducing a new storage model.
