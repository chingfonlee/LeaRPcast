## 1. Service DI Wiring

- [x] 1.1 Add `@AndroidEntryPoint` annotation to `PlaybackService`.
- [x] 1.2 Replace direct field initialisation with `@Inject lateinit var playerFactory: PlayerFactory` and `@Inject lateinit var mediaSessionFactory: MediaSessionFactory`.
- [x] 1.3 Update `onCreate` to use the injected factories.

## 2. Foreground Notification

- [x] 2.1 Verify Media3 automatic notification is active (no extra code needed if `MediaSession` is returned from `onGetSession`).
- [x] 2.2 Create a notification channel (`CHANNEL_ID = "playback_channel"`) in `LeaRPcastApplication` for Android 8+.
- [x] 2.3 Confirm notification appears during playback on device/emulator.

## 3. MediaController Wiring in App

- [x] 3.1 In `MainActivity`, retrieve the `SessionToken` via Hilt injection.
- [x] 3.2 Build a `MediaController` with `MediaController.Builder(this, sessionToken).buildAsync()`.
- [x] 3.3 Release `controllerFuture` in `onDestroy`.

## 4. Verification

- [x] 4.1 Run `./gradlew :app:assembleDebug` — build should pass.
- [x] 4.2 Launch app on emulator and confirm service starts without crash.
- [x] 4.3 Confirm foreground notification channel registration on Android 8+.
