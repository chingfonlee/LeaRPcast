## ADDED Requirements

### Requirement: Factory-Driven Media Initialization
The system MUST instantiate the primary media player and media session exclusively through designated factories to allow for centralized configuration and easier mocking.

#### Scenario: Service starts playback engine
- **WHEN** the `PlaybackService` calls `onCreate`
- **THEN** it resolves an `ExoPlayer` instance via `PlayerFactory`, and binds it using a `MediaSessionFactory`.

### Requirement: Playable Item Model
The domain MUST represent media items using a generic `PlayableItem` model that hides differences between Podcast episodes and Radio streams.

#### Scenario: Displaying current playing info
- **WHEN** a track starts
- **THEN** it is represented as a `PlayableItem` containing its title, subtitle, artwork URI, and a unique identifier (Media ID).

### Requirement: Playback State Abstraction
The UI layer MUST NOT depend on ExoPlayer states. Instead, a `PlaybackStateModel` MUST be provided to represent playback intent.

#### Scenario: UI observed states
- **WHEN** the player state changes
- **THEN** it maps to a domain `PlaybackStateModel` such as `IDLE`, `BUFFERING`, `PLAYING`, or `ERROR(PlaybackError)`.
