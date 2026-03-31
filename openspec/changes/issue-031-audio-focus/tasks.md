## 1. PlayerFactory Verification

- [x] 1.1 Confirm `DefaultPlayerFactory` sets `AudioAttributes(usage = USAGE_MEDIA, contentType = CONTENT_TYPE_MUSIC, handleAudioFocus = true)`.
- [x] 1.2 Confirm `setHandleAudioBecomingNoisy(true)` is called.

## 2. Snapshot Save

- [x] 2.1 In `PlaybackService`, override (or observe) `onIsPlayingChanged(isPlaying = false)` caused by focus loss.
- [x] 2.2 Create and store an `InterruptionSnapshot(positionMs, wasUserPause = false, timestampMs = now)`.

## 3. Conditional Resume

- [x] 3.1 On focus gain event (can observe via `AudioManager.OnAudioFocusChangeListener` if needed alongside ExoPlayer), retrieve snapshot and call `ResumeAfterInterruptionPolicy.shouldResume(snapshot, settings)`.
- [x] 3.2 If `true`, call `player.play()` and clear snapshot.

## 4. Testing

- [ ] 4.1 On emulator: place a call (using Android Extras) ??audio pauses ??hang up ??audio resumes.
