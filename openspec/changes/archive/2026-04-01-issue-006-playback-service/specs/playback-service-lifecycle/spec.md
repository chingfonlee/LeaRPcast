## ADDED Requirements

### Requirement: Hilt-Injected Service
`PlaybackService` MUST be annotated with `@AndroidEntryPoint` and receive `PlayerFactory` and `MediaSessionFactory` via `@Inject`.

#### Scenario: Service created with DI
- **WHEN** the OS starts `PlaybackService`
- **THEN** Hilt injects `playerFactory` and `mediaSessionFactory`, and `onCreate` calls them to create player and session.

### Requirement: Foreground Notification
`PlaybackService` MUST show a persistent foreground notification during active playback to comply with Android background execution rules.

#### Scenario: Playback starts
- **WHEN** a media item begins playing in the service
- **THEN** a foreground notification appears showing the media title and playback controls.

### Requirement: MediaController Connection Point
`:app` MUST provide a way for the UI to build a `MediaController` connected to `PlaybackService` via the injected `SessionToken`.

#### Scenario: Activity connects to service
- **WHEN** `MainActivity` is created
- **THEN** it builds a `MediaController` using the `SessionToken` from Hilt and passes it to UI-layer components.
