## 1. PlayerEventMapper

- [x] 1.1 Create `PlayerEventMapper.kt` in `:core:media`.
- [x] 1.2 Implement `fun mapState(playbackState: Int, playWhenReady: Boolean, error: PlaybackException?): PlaybackStateModel`.
- [x] 1.3 Map `STATE_IDLE ??Idle`, `STATE_BUFFERING ??Buffering`, `STATE_READY + playWhenReady ??Playing`, `STATE_READY + !playWhenReady ??Paused`, `STATE_ENDED ??Idle`, error ??`Error(PlaybackError)`.

## 2. PlaybackController Interface & Implementation

- [x] 2.1 Create `PlaybackController` interface in `:core:media` with `playbackState`, `currentItem`, `play()`, `pause()`, `seekTo()`.
- [x] 2.2 Create `DefaultPlaybackController` implementing the interface, holding a nullable `MediaController` reference.
- [x] 2.3 Register `Player.Listener` in `DefaultPlaybackController` that updates `MutableStateFlow` on relevant events.
- [x] 2.4 Implement `fun connectController(mediaController: MediaController)` so `MainActivity` can pass the controller after it's built.

## 3. Hilt Binding

- [x] 3.1 Bind `PlaybackController` ??`DefaultPlaybackController` in `MediaModule` as a `@Singleton`.

## 4. Verification

- [x] 4.1 Run `./gradlew :app:kspDebugKotlin` ??Hilt graph should compile.
- [x] 4.2 Write a simple unit test verifying `PlayerEventMapper` output for each `Player.STATE_*` value.
