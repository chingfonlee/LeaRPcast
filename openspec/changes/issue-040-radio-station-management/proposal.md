## Why

LeaRPcast's Radio tab can currently browse and play stations, but users cannot maintain their own station library. Adding custom stations and reordering them is needed so the Radio experience can reflect personal preferences and support stations that are not available through RadioBrowser.

## What Changes

- Add support for creating custom radio stations with a name and stream URL, plus optional metadata.
- Add support for editing and deleting existing stations from the user's radio library.
- Add support for changing station order so users can prioritize their preferred stations.
- Preserve user ordering and custom stations across app launches and RadioBrowser refreshes.

## Capabilities

### New Capabilities
- `radio-station-management`: user-managed radio station CRUD, ordering, and persistence.

### Modified Capabilities
- 

## Impact

- `:feature:radio` UI needs management affordances for add/edit/reorder actions.
- `:app/data/repository/DefaultRadioRepository.kt` must preserve user-managed ordering and custom stations during refresh.
- `:core:database` radio station persistence already carries `sort_order`, but the behavior around it becomes user-facing.
- `:domain` will need use cases for station management so the feature layer does not manipulate storage directly.
- `feature/radio/src/main/res/values/strings.xml` will need new UI strings for add/edit/reorder flows.
