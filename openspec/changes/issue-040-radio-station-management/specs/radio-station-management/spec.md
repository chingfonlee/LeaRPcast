## ADDED Requirements

### Requirement: Radio Station Management Actions
`RadioScreen` MUST provide user-visible actions to add, edit, delete, and reorder radio stations in the local radio library.

#### Scenario: User opens management controls
- **WHEN** the user views the Radio tab
- **THEN** the screen exposes an entry point to add a station and controls to edit, delete, and reorder existing stations.

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
