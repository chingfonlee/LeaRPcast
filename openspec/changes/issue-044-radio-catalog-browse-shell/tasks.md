## 1. Radio Shell Structure

- [x] 1.1 Define the shell-level Radio UI state in `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/model/` so Home, Stations, Search, Favorites, Player, loading, empty, buffering, playback failure, and network failure states are represented explicitly.
- [x] 1.2 Update `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/viewmodel/RadioViewModel.kt` to own navigation state, Taiwan catalog browsing state, search query state, sort state, favorite toggles, recent history, and playback context in one source of truth.
- [x] 1.3 Confirm the shell reuses the existing playback flow while consuming the normalized Taiwan catalog plus the supplemental global Radio Browser feed instead of introducing a new storage or playback engine.
- [x] 1.4 Add an app asset-backed Taiwan catalog source that parses `app/src/main/assets/radio/taiwan_radio_normalized_deduped.json`, maps the taxonomy fields into `RadioStation`, and merges the result into the local radio repository as the primary browse catalog.
- [x] 1.5 Extend the radio station schema and mappers so taxonomy columns and JSON-backed keyword lists persist through Room, including `ui_primary_group`, `network`, `region`, `search_keywords`, `aliases`, and `merged_from_ids`.

## 2. Home And Navigation UI

- [x] 2.1 Rework `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt` into a Radio-local shell inside the existing Radio destination using a top-aligned `ScrollableTabRow` for Home, Stations, Search, Favorites, and Player.
- [x] 2.2 Build the Home destination so it prioritizes current playback, saved stations, recent listening, and a catalog entry point.
- [x] 2.3 Add the persistent mini player and the full player expansion behavior so playback remains visible across the shell.
- [x] 2.4 Keep the touch targets and layout spacing mobile-friendly and usable with one hand.

## 3. Stations And Search UI

- [x] 3.1 Add grouped catalog browsing for categories, networks, and regions using the Taiwan JSON taxonomy fields (`ui_primary_group`, `network`, and `region`) so the Stations destination never falls back to a single flat list.
- [x] 3.2 Add search that matches station name, display name, frequency, band, source group, network, region, category, media type, primary/secondary UI groups, aliases, merged IDs, and search keywords from the normalized catalog.
- [x] 3.3 Add sorting controls for recent play, station name, and frequency.
- [x] 3.4 Add the empty-state UI for no favorites, no search results, and loading or network failure conditions.

## 4. Playback And Favorite Interactions

- [x] 4.1 Make the station row the primary playback target and keep the play control aligned with the same action.
- [x] 4.2 Add the favorite and unfavorite toggle as a secondary action that does not interrupt playback.
- [x] 4.3 Add the playback state labels for playing, loading, buffering, and failure states.
- [x] 4.4 Ensure the active station is highlighted consistently across Home, Stations, Search, Favorites, and Player.

## 5. Stream Resolution And Fallback

- [x] 5.1 Add a stream resolution step in the radio playback path so playlist-like URLs and indirect stream URLs can be normalized before they are passed to the player.
- [x] 5.2 Preserve both the primary stream URL and any fallback or resolved stream URL in the station model so playback can retry the alternate source when the primary one fails.
- [x] 5.3 Keep playback on the existing Media3 / ExoPlayer stack and avoid creating separate players for HLS, MP3, AAC, OGG, or extensionless direct streams.
- [x] 5.4 Map playlist-style sources such as `.pls`, `.m3u`, and `.xspf` to a resolved media URL or a recoverable playback error before entering the final failure state.
- [x] 5.5 Confirm that direct audio streams, HLS `.m3u8` streams, and redirecting HTTP endpoints can all be started from the same playback entry point.

## 6. Strings And Verification

- [x] 6.1 Add the required shell, navigation, browse, search, player, and empty-state strings to `feature/radio/src/main/res/values/strings.xml` using the key list in `design.md`, including taxonomy-aware labels for Taiwan categories, networks, and regions.
- [x] 6.2 Add any shell-level strings owned by the app container to `app/src/main/res/values/strings.xml` if the final composition requires them.
- [x] 6.2.1 Use the recommended Radio-local tab labels Home, Stations, Search, Favorites, and Player, plus the defined empty-state and player hierarchy copy.
- [x] 6.3 Add or update unit tests for stream resolution, fallback selection, and playback error classification, including playlist parsing and retry behavior.
- [x] 6.4 Add or update Compose UI tests for navigation, tap-to-play, favorites, search, empty states, player visibility, taxonomy-based station grouping, and fallback error messaging.
- [x] 6.5 Run the relevant Radio feature tests and a debug build verification to confirm the shell composes and the interactions remain stable.

## 7. Catalog Acceptance Notes

- [x] 7.1 Confirm Stations renders Taiwan taxonomy groups from the persisted JSON catalog rather than a hardcoded station list.
- [x] 7.2 Confirm global stations remain available in search but do not dominate the Stations browse surface.
- [x] 7.3 Confirm category, network, and region labels are sourced from `ui_primary_group`, `network`, and `region` respectively, with fallback buckets only when those values are missing.
- [x] 7.4 Confirm the repository merge keeps the Taiwan catalog entries authoritative when the same station exists in both the local catalog and the supplemental global feed.
