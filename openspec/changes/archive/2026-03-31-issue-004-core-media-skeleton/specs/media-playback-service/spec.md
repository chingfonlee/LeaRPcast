## ADDED Requirements

### Requirement: PlaybackService Lifecycle
The system MUST provide a background service that manages media playback independently of the application's UI lifecycle. It MUST use Jetpack Media3's `MediaSessionService`.

#### Scenario: Service Initialization
- **WHEN** the application requests media playback
- **THEN** `PlaybackService` is instantiated and an `ExoPlayer` instance is bound to it and integrated into a `MediaSession`.

### Requirement: Single ExoPlayer Instance
The system MUST guarantee only one active `ExoPlayer` instance is responsible for playback management, maintained inside the `PlaybackService`.

#### Scenario: Playback Request from Controller
- **WHEN** the `MediaController` receives a command to play an item
- **THEN** the single `ExoPlayer` managed by the service executes the playback logic.

### Requirement: Android Background Playback Permissions
The system MUST declare required foreground service definitions in the `AndroidManifest.xml` (like `FOREGROUND_SERVICE` and `FOREGROUND_SERVICE_MEDIA_PLAYBACK`) according to Android 14+ rules to prevent process termination during playback.

#### Scenario: Audio background mode
- **WHEN** the application minimizes while playing audio
- **THEN** the `PlaybackService` is elevated to foreground using standard Media3 notifications, preventing OS suspension.
