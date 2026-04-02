## Why

The current Radio experience needs to serve two different user jobs: fast access for fixed listeners and structured browsing for users who search by station name, frequency, network, or category. The existing surfaces are too flat for the size of the Taiwan radio catalog, so the Radio destination needs a dedicated in-tab catalog-and-player shell now.

## What Changes

- Add an in-Radio shell with clear entry points for Home, Stations, Search, Favorites, and Player, while keeping the app-level bottom navigation unchanged.
- Prioritize saved stations and recent listening on Home instead of showing a flat catalog list.
- Add a Stations browsing surface that groups the catalog by category, network, and region instead of exposing one long list.
- Add search that matches station name, display name, frequency, network, aliases, and keywords.
- Add a persistent mini player with an expandable full player so playback state is always visible.
- Add tap-to-play station rows, a secondary favorite toggle, and explicit playback state feedback.
- Add empty, loading, buffering, playback failure, and network failure states across the shell.
- Use the normalized Taiwan radio JSON catalog as the feature data source for browsing and ranking behavior.

## Capabilities

### New Capabilities
- `radio-catalog-browse-shell`: Android radio destination shell with home prioritization, station catalog browsing, search, favorites, recent listening, and persistent player behavior.

### Modified Capabilities
- 

## Impact

- `feature/radio` will need a shell-level UI structure, sectioned catalog browsing, and state handling for Home, Stations, Search, Favorites, and Player.
- `feature/radio/src/main/res/values/strings.xml` will need strings for navigation labels, section titles, empty states, filters, sort options, playback labels, and error feedback.
- `app/src/main/res/values/strings.xml` should remain compatible with the existing app-level bottom navigation; only Radio-local labels belong in the feature module unless the app container owns shared shell chrome.
- `feature/radio` will need to map the normalized station JSON into browse sections, filter chips, and search indexes.
- `:core:media` playback integration must remain compatible so the shell can reflect current playback without directly managing ExoPlayer.
