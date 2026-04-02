## ADDED Requirements

### Requirement: Discovery-First Entry Point
`RadioScreen` MUST provide a discovery-first entry path for users who want to add or find a station to hear next.

#### Scenario: User opens the discovery flow
- **WHEN** the user chooses to add or discover a station
- **THEN** the screen presents listening-oriented choices such as recommendations, search, or manual entry before asking for detailed station data.

### Requirement: Recommendation Paths
The system MUST provide recommended stations as a starting point for users who do not know what to choose.

#### Scenario: User has no station name
- **WHEN** the user does not know a station name or URL
- **THEN** the system can show a small list of recommended stations to start listening immediately.

#### Scenario: User has no location permission
- **WHEN** the app has no precise location access
- **THEN** recommendations still work using broad locale, language, or region defaults.

### Requirement: Search And Manual Entry
The system MUST provide both keyword search and a minimal manual entry path for users who already know what they want.

#### Scenario: User searches by keyword
- **WHEN** the user enters a station name or keyword
- **THEN** the system returns matching stations that can be previewed immediately.

#### Scenario: User enters a known station name and stream URL
- **WHEN** the user chooses manual advanced entry
- **THEN** the user can provide the station name and stream URL directly without having to use recommendations.

### Requirement: Preview Before Save
The system MUST allow a user to preview a station before saving it to the local radio library.

#### Scenario: User previews a recommended station
- **WHEN** the user taps a recommended or searched station
- **THEN** playback starts immediately and the user can save the station afterward if they want to keep it.

### Requirement: Lightweight Save
The system MUST let the user save a station after previewing it without forcing a long form-first flow.

#### Scenario: User saves after preview
- **WHEN** the user has previewed a station and chooses to keep it
- **THEN** the station is stored locally and becomes available in the radio library.

### Requirement: Discovery Bias
The system SHOULD bias discovery suggestions using broad locale, language, or region hints when those signals are available.

#### Scenario: App has language context
- **WHEN** the app knows the current language or broad region
- **THEN** the discovery flow can prioritize stations that match those signals.

### Requirement: Listening Utilities Are Secondary
Time-based listening features such as scheduled start or scheduled stop MUST be treated as future follow-up capabilities rather than part of the core discovery flow.

#### Scenario: User is using the MVP discovery flow
- **WHEN** the user is adding or previewing a station
- **THEN** the flow focuses on discovery, preview, and save without requiring scheduling controls.
