## 1. Create Core Media Models

- [x] 1.1 In `:core:model`, create `PlayableItem` data class representing a generic track (id, title, subtitle, imageUri, mediaUri).
- [x] 1.2 In `:core:model`, create sealed interface `PlaybackStateModel` (Idle, Buffering, Playing, Paused, Error).
- [x] 1.3 In `:core:model`, create sealed class `PlaybackError` defining known error types.
- [x] 1.4 In `:core:model`, create `InterruptionSnapshot` data class to save progress during interruptions.

## 2. ExoPlayer and Session Factories

- [x] 2.1 In `:core:media`, create `PlayerFactory` interface and `DefaultPlayerFactory` implementation that builds an `ExoPlayer` with standard `AudioAttributes`.
- [x] 2.2 Create `MediaSessionFactory` interface and `DefaultMediaSessionFactory` implementation.
- [x] 2.3 Update `MediaModule` to bind these factory implementations.

## 3. PlaybackService Refactoring

- [x] 3.1 Un-hardcode the `ExoPlayer.Builder` in `PlaybackService.onCreate()`.
- [x] 3.2 Add `@AndroidEntryPoint` to `PlaybackService`.
- [x] 3.3 `@Inject` `PlayerFactory` and `MediaSessionFactory` into `PlaybackService` and use them in `onCreate()`.

## 4. Verification

- [x] 4.1 Sync Project and clean build.
- [x] 4.2 Verify app compiles and service initializes correctly.

