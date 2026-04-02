## Why

LeaRPcast's Radio tab can currently browse and play stations, but the experience still needs to feel more like a place to listen than a place to manage records. Users should be able to discover something quickly, try it immediately, and save the stations they actually want to hear without friction. Custom stations and reordering still matter, but they should serve that listening-first flow.

## What Changes

- Add support for quick station discovery so users can start from recommendations, search, or manual entry depending on what they know.
- Add support for previewing a station before saving it, so users can hear something first and keep it only if they like it.
- Add support for creating custom radio stations with a name and stream URL, plus optional metadata, as a lightweight save flow.
- Add support for editing and deleting existing stations from the user's radio library.
- Add support for changing station order so users can prioritize the stations they actually want to hear next.
- Preserve user ordering and custom stations across app launches and RadioBrowser refreshes.

## Capabilities

### New Capabilities
- `radio-station-management`: listening-first radio discovery, save-for-later station management, ordering, and persistence.

### Modified Capabilities
- 

## Impact

- `:feature:radio` UI needs listening-first discovery, save, and secondary management affordances for add/edit/reorder actions.
- `:app/data/repository/DefaultRadioRepository.kt` must preserve user-managed ordering and custom stations during refresh.
- `:core:database` radio station persistence already carries `sort_order`, but the behavior around it becomes user-facing.
- `:domain` will need use cases for station management so the feature layer does not manipulate storage directly.
- `feature/radio/src/main/res/values/strings.xml` will need new UI strings for discovery, preview, save, add/edit, and reorder flows.
