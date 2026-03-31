## 1. ViewModel Update

- [x] 1.1 In `PodcastDetailViewModel`, `combine` episode list flow with `PlaybackController.currentItem` to produce `EpisodeUiState` with `currentlyPlayingId`.
- [x] 1.2 Inject `PlayEpisodeUseCase` into `PodcastDetailViewModel`.

## 2. EpisodeRow Update

- [x] 2.1 Update episode row composable to accept `isPlaying: Boolean` and show a different icon when true.
- [x] 2.2 Add `onPlay: () -> Unit` callback to episode row.

## 3. EpisodeListScreen Update

- [x] 3.1 Wire `onPlay` callback through `PodcastDetailViewModel.playEpisode(episode)`.
- [x] 3.2 Pass `currentlyPlayingId` to highlight active row.

## 4. Verification

- [ ] 4.1 Tap an episode; `MiniPlayer` appears.
- [ ] 4.2 That episode row is highlighted.
