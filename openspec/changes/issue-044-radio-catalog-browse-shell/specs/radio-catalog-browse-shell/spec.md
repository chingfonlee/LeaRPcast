## ADDED Requirements

### Requirement: Radio Shell Navigation
Within the existing Radio app destination, the system MUST present a Radio-local shell with Home, Stations, Search, Favorites, and Player destinations using a top-aligned `ScrollableTabRow` or an equivalent horizontally scrollable tab strip that preserves the current playback context when switching between internal tabs.

#### Scenario: User opens the Radio shell
- **WHEN** the user enters the Radio destination from the app-level bottom navigation for the first time in a session
- **THEN** the shell MUST show the Home destination and keep the current playback state available if one already exists

#### Scenario: User switches tabs during playback
- **WHEN** the user switches from one Radio-local destination to another while a station is playing
- **THEN** the system MUST keep the active station playing and continue to show the current playback state

### Requirement: Home Priority Order
The Home destination MUST prioritize current playback, saved stations, recent listening, and a path to browse the full catalog instead of showing a flat station list.

#### Scenario: Saved stations exist
- **WHEN** the user opens Home and saved stations exist
- **THEN** the screen MUST show the current playing item if present, then saved stations, then recent listening, and then a catalog entry point

#### Scenario: No saved stations exist
- **WHEN** the user opens Home and no saved stations exist
- **THEN** the screen MUST show an onboarding empty state with guidance, starter suggestions, and a clear add-favorite action

### Requirement: Radio Copy And Labels
The Radio-local shell MUST use short, literal tab labels and concise state copy so the interface stays readable on mobile.

#### Scenario: Tab labels are rendered
- **WHEN** the Radio-local shell renders its internal tabs
- **THEN** the system MUST use short labels such as Home, Stations, Search, Favorites, and Player

#### Scenario: Home empty state copy is shown
- **WHEN** the Home destination has no saved stations
- **THEN** the system MUST show copy that explains how to add favorites and a primary action that starts browsing or saving stations

#### Scenario: Search has no matches
- **WHEN** the user searches and no station matches the query
- **THEN** the system MUST show a no-results message that references the query and a clear action to refine or clear the search

#### Scenario: Player needs attention
- **WHEN** the player is buffering or has failed
- **THEN** the system MUST show a short state label and a single clear action such as Retry or Play again

### Requirement: Structured Station Browsing
The Stations destination MUST present the catalog in grouped sections and filterable taxonomy dimensions instead of a single long ungrouped list.

#### Scenario: User browses by category
- **WHEN** the user selects a category filter such as news, music, public service, or education
- **THEN** the system MUST show only stations whose taxonomy browse label matches the selected category grouping

#### Scenario: User browses by network
- **WHEN** the user selects a network grouping such as 中廣 or ASIAFM
- **THEN** the system MUST show stations under that network grouping without flattening the entire catalog

#### Scenario: User browses by region
- **WHEN** the user selects a region grouping such as 台北 or 高雄
- **THEN** the system MUST show stations under that region grouping without flattening the entire catalog

### Requirement: Catalog Sorting
The Stations destination MUST allow sorting by recent play, station name, and frequency.

#### Scenario: User sorts by name
- **WHEN** the user selects name sorting
- **THEN** the system MUST reorder the visible station list alphabetically by the display name or name fallback

#### Scenario: User sorts by frequency
- **WHEN** the user selects frequency sorting
- **THEN** the system MUST reorder the visible station list by the station frequency value and keep stations without a usable frequency at the end

### Requirement: Catalog Search
The Search destination MUST match station name, display name, display frequency, frequency, band, source group, network, region, category, media type, taxonomy groups, aliases, merged IDs, and search keywords.

#### Scenario: User searches by frequency
- **WHEN** the user enters a frequency such as 98.1
- **THEN** the system MUST return stations whose frequency-related or display frequency fields match the query

#### Scenario: User searches by network or alias
- **WHEN** the user enters a network name or alias
- **THEN** the system MUST return stations whose network, aliases, merged IDs, or keyword fields match the query

#### Scenario: Search yields no result
- **WHEN** the user enters a query that matches no station
- **THEN** the system MUST show a no-results empty state with a clear way to clear or refine the query

### Requirement: One-Tap Station Playback
The system MUST allow a station row to start playback when tapped and MUST keep the row tap behavior distinct from the favorite toggle.

#### Scenario: User taps a station row
- **WHEN** the user taps a station row
- **THEN** the system MUST start playback of that station without opening an intermediate detail page

#### Scenario: User taps the play control
- **WHEN** the user taps the explicit play control on a station row
- **THEN** the system MUST perform the same playback action as tapping the row

#### Scenario: User taps the favorite icon
- **WHEN** the user taps the favorite or unfavorite icon on a station row
- **THEN** the system MUST toggle the saved state without interrupting playback

### Requirement: Stream URL Compatibility
The system MUST be able to play direct radio stream URLs without requiring a different player per stream type and MUST treat HLS, direct audio streams, and stream redirects as supported playback inputs.

#### Scenario: User plays an HLS stream
- **WHEN** a station provides an HLS stream URL such as a `.m3u8` source
- **THEN** the system MUST pass the stream to the radio player as a normal media item and let the playback engine handle the HLS format

#### Scenario: User plays a direct audio stream
- **WHEN** a station provides a direct audio stream such as an MP3, M4A, OGG, or extensionless HTTP stream endpoint
- **THEN** the system MUST pass the URL to the radio player without requiring a special per-format player implementation

#### Scenario: User plays a redirecting stream
- **WHEN** a station stream URL resolves through an HTTP redirect to a playable audio endpoint
- **THEN** the system MUST allow the resolved stream to play as long as the final target is a supported media source

### Requirement: Stream Fallback And Recovery
The system MUST attempt a fallback stream when the primary stream fails and a fallback value is available in the station data.

#### Scenario: Primary stream fails and fallback exists
- **WHEN** playback fails for a station that has a fallback or resolved stream URL available
- **THEN** the system MUST attempt the fallback stream before surfacing a final playback failure state

#### Scenario: Playlist-based source needs resolution
- **WHEN** a station source is a playlist file such as `.pls`, `.m3u`, or `.xspf` rather than a direct media URL
- **THEN** the system MUST resolve the playlist to a playable media URL before handing it to the player, or show a recoverable playback failure if resolution is not possible

#### Scenario: No playable stream can be resolved
- **WHEN** neither the primary stream nor a fallback stream can be played
- **THEN** the system MUST show a playback failure state and preserve the station context so the user can retry or choose another station

### Requirement: Playback Status Visibility
The system MUST clearly indicate the active station and the current playback state across the shell.

#### Scenario: Station is playing
- **WHEN** playback is active
- **THEN** the system MUST highlight the active station and show the current playing state in the player area

#### Scenario: Station is buffering
- **WHEN** a selected station is buffering or loading
- **THEN** the system MUST show a loading or buffering state without clearing the station context

#### Scenario: Playback fails
- **WHEN** playback cannot start or is interrupted by an error
- **THEN** the system MUST show a playback failure state with a recoverable action

### Requirement: Persistent Player Surface
The system MUST provide a mini player and a full player that expose the current station name, frequency, playback control, favorite control, buffering state, and error state while remaining visible across Radio-local tabs and independent of the app-level bottom navigation.

#### Scenario: User sees the mini player
- **WHEN** a station is playing or paused
- **THEN** the mini player MUST remain visible while the user navigates between Radio destinations

#### Scenario: User opens the full player
- **WHEN** the user taps the mini player
- **THEN** the system MUST expand to the full player view with the detailed playback controls and metadata

### Requirement: Player Text Hierarchy
The full player MUST present the station name as the primary text, the frequency and network or region as the secondary text, and the playback state as the tertiary text.

#### Scenario: Player is showing station metadata
- **WHEN** the full player is open for a station
- **THEN** the station name MUST appear as the top text, the frequency and network or region MUST appear below it, and the playback state MUST appear below the metadata

#### Scenario: Player shows an error
- **WHEN** playback fails
- **THEN** the player MUST show a short error message and a retry action without removing the station context

### Requirement: Empty And Recovery States
The system MUST provide loading, empty, network failure, and recovery states without losing the last usable context when possible.

#### Scenario: Data is loading
- **WHEN** the Radio shell is fetching catalog or user station data
- **THEN** the system MUST show a loading state instead of an incomplete catalog

#### Scenario: Network request fails
- **WHEN** the system cannot load or refresh catalog data because of a network failure
- **THEN** the system MUST show a network failure state and provide retry behavior

#### Scenario: Saved stations are missing
- **WHEN** the user has no saved stations
- **THEN** the system MUST show a helpful empty state and a clear action to add favorites

### Requirement: Taxonomy-Driven Defaults
The system MUST use the normalized catalog metadata when available to derive browse groups, labels, and default ordering for the Taiwan-first catalog shell.

#### Scenario: Primary group metadata exists
- **WHEN** a station has a `ui_primary_group` value
- **THEN** the system MUST place the station into the matching browse group for the Stations destination and use that value as the category label source

#### Scenario: Search keywords exist
- **WHEN** a station provides `search_keywords`
- **THEN** the system MUST include those keywords in the search index

#### Scenario: Metadata is incomplete
- **WHEN** a station lacks one or more taxonomy fields
- **THEN** the system MUST fall back to name, display name, network, region, country, or frequency fields instead of hiding the station
