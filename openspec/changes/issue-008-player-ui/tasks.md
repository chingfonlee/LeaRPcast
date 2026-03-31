## 1. PlayerUiState & ViewModel

- [x] 1.1 Create `PlayerUiState` data class in `:feature:player` (`currentItem: PlayableItem?`, `isPlaying: Boolean`, `isBuffering: Boolean`).
- [x] 1.2 Create `PlayerViewModel` (Hilt `@HiltViewModel`) that injects `PlaybackController`.
- [x] 1.3 Map `PlaybackController.playbackState` and `currentItem` flows into `PlayerUiState` via `combine`.

## 2. PlayerScreen Composable

- [x] 2.1 Create `PlayerScreen.kt` in `feature/player/src/main/java/.../ui/screen/`.
- [x] 2.2 Render artwork placeholder (`Box` with background colour), title `Text`, subtitle `Text`.
- [x] 2.3 Add a play/pause `IconButton` that calls `viewModel.playOrPause()`.
- [x] 2.4 Add string keys (`player_play_button`, `player_pause_button`, `player_unknown_title`) to `:feature:player/src/main/res/values/strings.xml`.

## 3. MiniPlayer Composable

- [x] 3.1 Create `MiniPlayer.kt` in `feature/player/src/main/java/.../ui/component/`.
- [x] 3.2 Render a `Row` with title, play/pause `IconButton`.
- [x] 3.3 Set `clickable` to navigate to `AppDestinations.PLAYER`.
- [x] 3.4 Add string keys (`mini_player_content_description`) to strings.xml.

## 4. App Shell Integration

- [x] 4.1 In `LeaRPcastApp.kt`, add `MiniPlayer` to the `bottomBar` composable stacked above `NavigationBar`.
- [x] 4.2 Conditionally show `MiniPlayer` only when `currentItem != null`.
- [x] 4.3 Connect `AppNavHost` `PLAYER` destination to `PlayerScreen`.

## 5. Verification

- [x] 5.1 Run `./gradlew :app:assembleDebug`.
- [ ] 5.2 Verify `PlayerScreen` renders without crash on emulator.
- [ ] 5.3 Verify `MiniPlayer` is hidden when nothing is playing and visible when a `PlayableItem` is set.
