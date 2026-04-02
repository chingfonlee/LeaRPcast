## ADDED Requirements

### Requirement: Listening-First Radio Home
The Radio screen MUST prioritize saved or configured stations above secondary management actions when the page opens.

#### Scenario: User opens Radio with saved stations
- **WHEN** the user opens the Radio page and saved stations exist
- **THEN** the screen shows the saved stations immediately and keeps playback actions visible before any secondary controls

#### Scenario: User opens Radio with no saved stations
- **WHEN** the user opens the Radio page and no saved stations exist
- **THEN** the screen shows an empty state instead of an empty list

### Requirement: One-Tap Station Playback
The Radio screen MUST allow the user to start playback of a station by tapping the station row or its explicit play control.

#### Scenario: User taps a saved station
- **WHEN** the user taps a station row or play button
- **THEN** the system starts playback of that station without requiring a confirmation step

#### Scenario: User switches stations
- **WHEN** the user taps a different station while one is already playing
- **THEN** the system switches playback to the newly selected station and updates the playing indicator

### Requirement: Favorite Management Is Secondary
The Radio screen MUST provide an explicit add-favorite action and a favorite toggle for saved stations without making those actions more prominent than playback.

#### Scenario: User wants to add a favorite station
- **WHEN** the user taps the add-favorite action
- **THEN** the system opens the add-favorite flow from the Radio page

#### Scenario: User toggles a favorite state
- **WHEN** the user taps the favorite or unfavorite control on a station
- **THEN** the system updates the saved state and reflects the change in the list

### Requirement: Playback Status Visibility
The Radio screen MUST show the current playback state clearly while the user remains on the page.

#### Scenario: Station is playing
- **WHEN** playback is active
- **THEN** the screen shows which station is playing and marks that station as the active item

#### Scenario: Playback fails
- **WHEN** playback cannot start or is interrupted by an error
- **THEN** the screen shows a playback failure state with a recoverable action

### Requirement: Loading And Network Recovery
The Radio screen MUST expose loading and network failure states without losing the user's last known context when possible.

#### Scenario: Radio data is loading
- **WHEN** the page is fetching station data
- **THEN** the screen shows a loading state instead of an incomplete list

#### Scenario: Network request fails
- **WHEN** the screen cannot load or refresh radio data because of a network issue
- **THEN** the screen shows a network error state and offers retry

### Requirement: Empty State Guidance
The Radio screen MUST provide a helpful empty state when no stations have been saved yet.

#### Scenario: First-time user has no saved stations
- **WHEN** the user has not saved any stations
- **THEN** the screen explains that no stations are saved and presents a clear add-favorite action

#### Scenario: User needs a quick first step
- **WHEN** the user is in the empty state
- **THEN** the screen may offer a small set of starter suggestions if they are available, but it still preserves add-favorite as the primary next step

