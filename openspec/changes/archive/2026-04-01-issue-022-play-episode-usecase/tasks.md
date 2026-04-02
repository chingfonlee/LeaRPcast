## 1. Mapper

- [x] 1.1 Create `EpisodePlayableMapper.kt` converting `Episode + uri: String` → `PlayableItem`.

## 2. Use Case

- [x] 2.1 Create `PlayEpisodeUseCase` in `:domain` injecting `PlaybackController`.
- [x] 2.2 Resolve URI: if `episode.isDownloaded` → use `episode.localFilePath`; else → use `episode.audioUrl`.
- [x] 2.3 Call `playbackController.setItem(playableItem)` then `.play()`.

## 3. Verification

- [x] 3.1 Unit test: assert local URI used when `isDownloaded = true`.
- [x] 3.2 Unit test: assert remote URL used when `isDownloaded = false`.
