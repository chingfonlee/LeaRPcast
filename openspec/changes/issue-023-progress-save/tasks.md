## 1. Use Case

- [x] 1.1 Create `SavePlaybackProgressUseCase` in `:domain` injecting `EpisodeRepository` (for `updateProgress(episodeId, positionMs)`).
- [x] 1.2 Implement `suspend operator fun invoke(episodeId: String, positionMs: Long, isCompleted: Boolean)`.

## 2. PlaybackService Integration

- [x] 2.1 In `PlaybackService.onCreate`, launch a coroutine that collects a `ticker` (30s interval) and calls use case with `player.currentPosition`.
- [x] 2.2 In `Player.Listener.onPlaybackStateChanged`, call use case immediately when transitioning to `STATE_ENDED`.
- [x] 2.3 In `onIsPlayingChanged(isPlaying = false)`, call use case with `isCompleted = false`.

## 3. Verification

- [ ] 3.1 Play an episode for 35 seconds, kill app, relaunch, and confirm `PlaybackProgressEntity` in DB has updated position.
