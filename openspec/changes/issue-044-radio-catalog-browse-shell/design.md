## Context

The Radio experience now needs to serve a larger and more varied catalog than a single listening-home surface can handle. Fixed listeners still need a fast path to favorite stations, but the Radio destination also needs structured browsing, search, and a persistent player shell so users can move between discovery and playback without losing context.

This change builds on the existing Radio module and the listening-first pattern introduced in `issue-043-radio-listening-home`, but expands the Radio destination into a broader catalog browser. The normalized Taiwan radio JSON is the primary catalog source for the Stations and Favorites experience, and it provides the taxonomy for categories, networks, regions, display labels, search keywords, aliases, and merged station IDs. The existing remote Radio Browser feed remains available as a supplemental source for global search and fallback discovery.

The catalog also contains mixed stream URL forms, including direct HTTP audio endpoints, HLS `.m3u8` streams, and playlist-like sources. The design should therefore rely on a single playback engine with a lightweight stream resolution and fallback layer, rather than separate players per format.

For implementation, the normalized Taiwan JSON is treated as the authoritative browse catalog for the Radio shell. The app should load it from an asset-backed catalog source, map the taxonomy fields into the station model, and persist those fields in Room so the browse experience is stable across refreshes and app restarts.

Relevant paths:
- `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/`
- `feature/radio/src/main/res/values/strings.xml`
- `app/src/main/res/values/strings.xml`
- `core/media`

## Goals / Non-Goals

**Goals:**
- Make Radio feel like a single coherent shell for browsing and playback.
- Keep Home focused on favorites, recent listening, and the current player state.
- Make Stations a structured catalog browser rather than a flat list.
- Make search precise enough for station name, frequency, network, and alias lookup.
- Keep the currently playing station visible across the shell.
- Preserve mobile-friendly one-tap playback and a low-learning-cost interaction model.

**Non-Goals:**
- Adding cloud sync or account-backed cross-device favorites.
- Replacing the existing playback service or changing the media engine architecture.
- Introducing a separate management-only page for catalog editing.
- Designing recommendation algorithms beyond the catalog taxonomy and basic recency ordering.

## Decisions

1. Keep the existing app-level bottom navigation unchanged and place a secondary Radio-local shell inside the Radio destination.
   - Why: The app already has top-level Radio, Podcast, Download, and Setting navigation. Adding another global bottom nav would create nested navigation conflicts and confuse the user.
   - Alternatives considered: replacing the app-level bottom navigation or making Home, Stations, Search, Favorites, and Player app-wide tabs. Those options would blur app sections and make the Radio shell feel like a second app.

2. Use a Radio-local `ScrollableTabRow` as the primary shell switcher for Home, Stations, Search, Favorites, and Player.
   - Why: Five destinations are too many for a fixed `TabRow` and far too many for a `SegmentedButton`. A `ScrollableTabRow` keeps the labels readable, scales to mobile widths, and still feels lightweight inside the Radio destination.
   - Alternatives considered: a standard `TabRow`, `SegmentedButton`, or another bottom navigation bar inside Radio. `TabRow` can compress too much on smaller screens, `SegmentedButton` is better suited to 2-4 choices, and a second bottom nav would conflict with the app shell.

   Recommended labels:
   - Home
   - Stations
   - Search
   - Favorites
   - Player

   Copy intent:
   - Keep tab labels short and literal.
   - Use the tab content, not the tab label, to explain the page purpose.
   - Reserve "Now playing" for status text inside the player area rather than as the tab label.

3. Keep Home listening-first and move catalog breadth to Stations.
   - Why: Fixed listeners need a fast entry point to favorites and recent listening, while discovery users need structure. Mixing the two makes the page feel crowded.
   - Alternatives considered: a single universal list. That would be simpler to build but would fail the "fast access first" requirement and would not scale with catalog size.

4. Derive browsing sections from the normalized Taiwan catalog taxonomy and persist the fields in the station model.
   - Why: The catalog already contains `ui_primary_group`, `ui_secondary_group`, `network`, `region`, `display_name`, `display_frequency`, `search_keywords`, `aliases`, and `merged_from_ids`. Those fields should be the source of truth for browsing, searching, and sorting rather than UI-side heuristics.
   - Alternatives considered: hardcoded category lists or host-based inference. Those approaches are brittle and would drift from the normalized catalog.

   Catalog source contract:
   - Primary browse source: `app/src/main/assets/radio/taiwan_radio_normalized_deduped.json`
   - Primary grouping fields:
     - Categories: `ui_primary_group`
     - Networks: `network`
     - Regions: `region`
   - Search fields:
     - `display_name`
     - `display_frequency`
     - `frequency`
     - `band`
     - `network`
     - `region`
     - `ui_primary_group`
     - `ui_secondary_group`
     - `search_keywords`
     - `aliases`
     - `merged_from_ids`
   - Fallback rules:
     - If `ui_primary_group` is missing, fall back to `ui_secondary_group`, then `category`.
     - If `network` is missing, keep the station in a generic "Other" network bucket rather than inventing a label.
     - If `region` is missing, fall back to a neutral region bucket such as "Other" or "Unknown" instead of deriving a misleading city.
     - If `country` or `countryCode` are missing in the Taiwan catalog, default them to Taiwan metadata so Stations stays Taiwan-first.
   - Merge rules:
     - Keep Taiwan catalog entries as the browse source of truth for Stations and Favorites.
     - Merge supplemental remote Radio Browser entries by `id` when they do not collide with a Taiwan catalog station.
     - Preserve the primary catalog version when the same station appears in both sources.

5. Make station rows the primary playback target and keep favorite toggles secondary.
   - Why: The highest-frequency action is "play this station now," so the row tap must be the fast path.
   - Alternatives considered: requiring a separate play button or opening a station detail screen first. Both add friction for regular listening.

6. Use a persistent mini player with an expandable full player.
   - Why: Users must always know what is playing, and playback should survive navigation changes without forcing a page switch.
   - Alternatives considered: a hidden playback screen or a global toast-like cue. Neither gives enough state visibility.

   Player text hierarchy:
   - Primary line: station name
   - Secondary line: frequency and network or region
   - Tertiary line: playback state such as Playing, Buffering, or Paused
   - Control labels: Play, Pause, Favorite, Retry
   - Error copy: one short sentence that explains the failure and offers retry

7. Keep stream handling inside the playback pipeline as resolve-then-play, not as separate player implementations.
   - Why: The catalog mixes direct streams, HLS, and extensionless endpoints, and ExoPlayer already handles the supported media sources well once the URL is normalized.
   - Alternatives considered: building dedicated players for HLS, MP3, AAC, and playlist URLs. That would duplicate behavior and make error handling inconsistent.

8. Model the shell with explicit UI state instead of inferring from scattered flags.
   - Why: The page must support loading, empty, playing, buffering, playback failure, and network failure states across multiple tabs.
   - Alternatives considered: separate booleans per tab. That tends to create inconsistent edge cases and makes the UI harder to test.

9. Treat fallback stream URLs as a recovery path, not a separate destination.
   - Why: The data model already carries a primary stream and an alternate/resolved stream, so playback should retry the fallback before surfacing a final failure.
   - Alternatives considered: forcing the user to manually retry with a different player or opening a diagnostic flow. That adds friction and is not needed for the normal listening path.

10. Keep the Taiwan taxonomy visible in the UI only where it helps browsing, and hide global stations from Stations unless the user explicitly searches for them.
   - Why: The requirement is Taiwan-first browsing with global discovery only through search, which keeps the Stations page focused and less overwhelming.
   - Alternatives considered: mixing all global stations into Stations or adding a separate world-radio browse tab. Both would dilute the Taiwan-first browse model and reintroduce the long-list problem.

## Risks / Trade-offs

- [Risk] A secondary Radio shell can feel redundant if it looks too much like app-level navigation.
  - [Mitigation] Use a top-aligned `ScrollableTabRow` inside Radio, and keep the existing app-level bottom navigation unchanged.
- [Risk] A grouped catalog can become visually dense on smaller screens.
  - [Mitigation] Use compact rows, collapsible sections, and filter chips instead of large cards.
- [Risk] Search across multiple station attributes may feel expensive on large catalogs.
  - [Mitigation] Build an in-memory search index from normalized fields and debounce input changes.
- [Risk] The persistent mini player can compete with content on short screens.
  - [Mitigation] Keep the mini player compact and reserve the full player for detailed playback actions.
- [Risk] Some stations may publish playlist URLs or indirect stream URLs that require resolution before playback.
  - [Mitigation] Add a stream resolution step that can unwrap common playlist formats and preserve the original URL as fallback.
- [Risk] Stream failures may be hard to distinguish from unsupported formats.
  - [Mitigation] Map playback errors into user-facing states and try the fallback stream before showing a failure.

## Migration Plan

- Add the Taiwan catalog as an app asset and load it into the app repository at startup or refresh time.
- Extend the Room `radio_station` schema with taxonomy columns plus JSON-backed list fields so the normalized catalog can round-trip through local storage.
- Keep the existing playback pipeline and service contract, but enrich station metadata before playback and search.
- Merge the Taiwan catalog into the existing local radio cache, then keep the remote Radio Browser feed as a supplemental source for global search and fallback discovery. During merge, deduplicate by stable station id and prefer the Taiwan catalog record for taxonomy fields when both sources expose the same station.
- If the current Radio entry already points to a listening-first home, the new shell should absorb that home into the Home tab rather than duplicating it.

## Taxonomy Implementation Notes

- The taxonomy source is intentionally data-driven; the UI should not hardcode the Taiwan category, network, or region lists beyond display fallback labels.
- The catalog source should read the JSON asset once, transform each entry into a `RadioStation`, and store the taxonomy fields in the local repository so the shell can browse without re-parsing the asset on every navigation.
- Stations grouping should be rendered from the persisted fields in this order:
  1. Taiwan categories via `ui_primary_group`
  2. Taiwan networks via `network`
  3. Taiwan regions via `region`
- Search should continue to work across all stations, including supplemental global entries, but the Stations browse surface should remain Taiwan-first.

## Open Questions

- Should the default landing tab always be Home, or should the shell restore the last visited tab?
- Should Favorites and Recent remain tab destinations, or should Recent stay Home-only while Favorites gets its own tab?
- Should the Player tab show a full-screen view only when active, or should it also expose queue-like controls later?
- Should the Taiwan catalog continue to be the primary local shell source while global stations remain search-only, or should a future release surface global stations in a separate browse tab?

## Suggested Copy

### Radio-local tabs
- Home
- Stations
- Search
- Favorites
- Player

### Home empty state
- Title: No saved stations yet
- Body: Save a station to make it easy to listen again.
- Primary action: Add favorites
- Secondary action: Browse stations

### Favorites empty state
- Title: Your favorites are empty
- Body: Add stations you listen to often so they stay one tap away.
- Primary action: Browse stations

### Search empty state
- No results for "{query}"
- Try a different station name, frequency, or network.
- Primary action: Clear search

### Loading state
- Title: Loading stations
- Body: Fetching your saved and recent stations now.

### Network failure state
- Title: Could not load stations
- Body: Check your connection and try again.
- Primary action: Retry

### Playback state labels
- Playing now
- Buffering
- Paused
- Playback failed

### Player block hierarchy
- Station name
- Frequency / network / region
- Playback state
- Play / Pause control
- Favorite toggle
- Retry action when needed

## Suggested String Keys

### App-level navigation
These already exist in `app/src/main/res/values/strings.xml` and can be reused as-is.

- `nav_radio`
- `nav_podcast`
- `nav_downloads`
- `nav_settings`

### Radio-local tabs
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_shell_tab_home` = Home
- `radio_shell_tab_stations` = Stations
- `radio_shell_tab_search` = Search
- `radio_shell_tab_favorites` = Favorites
- `radio_shell_tab_player` = Player

### Loading and recovery
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_shell_loading_title` = Loading stations
- `radio_shell_loading_body` = Fetching your saved and recent stations now.
- `radio_shell_network_failure_title` = Could not load stations
- `radio_shell_network_failure_body` = Check your connection and try again.
- `radio_shell_retry_button` = Retry

### Home empty state
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_home_empty_title` = No saved stations yet
- `radio_home_empty_body` = Save a station to make it easy to listen again.
- `radio_home_empty_primary_action` = Add favorites
- `radio_home_empty_secondary_action` = Browse stations

### Favorites empty state
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_favorites_empty_title` = Your favorites are empty
- `radio_favorites_empty_body` = Add stations you listen to often so they stay one tap away.
- `radio_favorites_empty_primary_action` = Browse stations

### Search empty state
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_search_empty_title` = No results for "%1$s"
- `radio_search_empty_body` = Try a different station name, frequency, or network.
- `radio_search_empty_clear_action` = Clear search
- `radio_search_empty_refine_action` = Refine search

### Player text and actions
These belong in `feature/radio/src/main/res/values/strings.xml`.

- `radio_player_now_playing_label` = Now playing
- `radio_player_state_playing` = Playing now
- `radio_player_state_buffering` = Buffering
- `radio_player_state_paused` = Paused
- `radio_player_state_failed` = Playback failed
- `radio_player_station_name_label` = Station
- `radio_player_metadata_template` = %1$s · %2$s
- `radio_player_play_button` = Play
- `radio_player_pause_button` = Pause
- `radio_player_favorite_button` = Add to favorites
- `radio_player_unfavorite_button` = Remove from favorites
- `radio_player_retry_button` = Play again

### Suggested reuse notes
- Reuse the existing `radio_home_title` only if the inner shell still wants a generic page title.
- Reuse `radio_retry_button`, `radio_favorite_button`, `radio_unfavorite_button`, and `radio_play_button` only if the implementation keeps the current semantics intact.
- Prefer the new `radio_shell_*` and `radio_player_*` keys for the inner shell so the strings stay easy to search and update.

