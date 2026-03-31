## 1. Verification of ExoPlayer Handling

- [x] 1.1 Confirm `DefaultPlayerFactory` calls `setHandleAudioBecomingNoisy(true)`.
- [x] 1.2 If missing, add `player.setHandleAudioBecomingNoisy(true)` in `DefaultPlayerFactory`.

## 2. Device Testing

- [ ] 2.1 Connect wired headphones → start playback → unplug → verify pause.
- [ ] 2.2 Confirm in emulator via `adb shell input keyevent KEYCODE_HEADSETHOOK` (simulated).

## 3. Fallback (if needed)

- [ ] 3.1 If ExoPlayer handling fails, add `BecomingNoisyReceiver : BroadcastReceiver` in `:core:media` registered in `PlaybackService`.

## 4. Verification

- [ ] 4.1 Confirm test passes on physical or emulator; document in QA checklist (ISSUE-036).
