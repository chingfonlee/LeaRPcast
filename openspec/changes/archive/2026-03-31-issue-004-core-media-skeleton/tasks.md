## 1. Setup Media3 Dependencies

- [x] 1.1 Add required Media3 libraries (`androidx.media3:media3-exoplayer`, `media3-session`, `media3-ui`, etc.) to `:core:media` `build.gradle.kts`.
- [x] 1.2 Add Media3 common dependency to `:domain` or `:core:common` if Playable item definitions require it (or mock them temporarily).

## 2. Core PlaybackService Implementation

- [x] 2.1 Create `PlaybackService` class in `:core:media:service` extending Jetpack Media3 `MediaSessionService`.
- [x] 2.2 Inside `PlaybackService`, instantiate a singleton `ExoPlayer` via `ExoPlayer.Builder`.
- [x] 2.3 Initialize `MediaSession.Builder(this, player).build()` in the `onCreate` lifecycle method.
- [x] 2.4 Ensure proper connection cleanup (`player.release()`, `mediaSession.release()`) in `onDestroy`.

## 3. Service Registration

- [x] 3.1 Declare `PlaybackService` in `:core:media`'s `AndroidManifest.xml` with intent filter `androidx.media3.session.MediaSessionService`.
- [x] 3.2 Add foreground service permissions (`FOREGROUND_SERVICE` and `FOREGROUND_SERVICE_MEDIA_PLAYBACK`) in `AndroidManifest.xml`.

## 4. Hilt Dependency Injection Setup

- [x] 4.1 Update `MediaModule` in `:core:media` to provide the `SessionToken` or a wrapper that allowing UI controllers to bind to `PlaybackService`.
- [x] 4.2 Validate DI graph by running `./gradlew :app:kspDebugKotlin`.

