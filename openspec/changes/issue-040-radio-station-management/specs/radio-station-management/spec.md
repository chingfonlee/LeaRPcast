## ADDED Requirements

### Requirement: Listening-First Radio Entry Point
`RadioScreen` MUST present Radio as a place to start listening first, with station discovery and quick add actions visible before secondary management controls.

#### Scenario: User opens the Radio tab
- **WHEN** the user views the Radio tab
- **THEN** the screen shows a listening-oriented entry point such as now playing, quick add, or recommended stations before secondary management controls.

#### Scenario: User wants a simple path to add a station
- **WHEN** the user does not already know a station URL
- **THEN** the screen offers discovery-first options such as recommended stations, search, or an advanced manual path without forcing a full form upfront.

### Requirement: Preview Before Save
`RadioScreen` MUST allow the user to start playback of a station before saving it to the local radio library.

#### Scenario: User previews a station before saving
- **WHEN** the user taps a recommended or searched station
- **THEN** the station starts playing immediately and the user can save it afterward if they want to keep it.

### Requirement: Secondary Station Management
`RadioScreen` MUST allow the user to edit and delete saved stations as secondary actions that remain available after the listening flow.

#### Scenario: User edits a saved station
- **WHEN** the user opens an existing saved station for editing
- **THEN** the user can update its saved details and keep it in the radio library without losing its ordering.

#### Scenario: User deletes a station
- **WHEN** the user deletes a station from the library
- **THEN** the station is removed from the local radio list and no longer appears after app relaunch.

### Requirement: Custom Station Creation
The system MUST allow the user to create a custom radio station using at minimum a station name and stream URL.

#### Scenario: User saves a valid custom station
- **WHEN** the user enters a valid name and stream URL and saves the station
- **THEN** the station is persisted locally, appears in the radio library, and can be played like other stations.

#### Scenario: User enters invalid station data
- **WHEN** the user tries to save a station without a required name or stream URL
- **THEN** the save action is rejected and the user sees validation feedback.

### Requirement: Station Ordering
The system MUST preserve user-defined station ordering and allow the user to change the order of stations in the radio library.

#### Scenario: User reorders stations
- **WHEN** the user moves a station to a new position
- **THEN** the new order is persisted and reflected after the app restarts.

#### Scenario: RadioBrowser refresh runs
- **WHEN** the radio repository refreshes stations from RadioBrowser
- **THEN** existing user-defined ordering remains intact and custom stations are not removed.

### Requirement: Location-Aware Suggestions
The system SHOULD be able to bias discovery suggestions using broad locale, language, or region signals when available, without making precise location access a requirement for basic use.

#### Scenario: User has not granted location permission
- **WHEN** the app has no precise location access
- **THEN** the discovery flow still works using locale or language defaults and does not block the user from adding or playing a station.

### Requirement: Listening Utilities Are Secondary
Time-based listening features such as scheduled start or scheduled stop MUST be treated as follow-up capabilities rather than part of the core add-and-save flow.

#### Scenario: User is adding a station
- **WHEN** the user is in the quick add flow
- **THEN** the flow focuses on discovery, preview, and save, and does not require scheduling controls to complete the primary task.
