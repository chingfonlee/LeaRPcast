# play-episode-use-case Specification

## Purpose
TBD - created by archiving change issue-022-play-episode-usecase. Update Purpose after archive.
## Requirements
### Requirement: Local-First Episode Playback
`PlayEpisodeUseCase` MUST use the locally downloaded file URI if `episode.isDownloaded == true`, otherwise the remote audio URL.

#### Scenario: Playing a downloaded episode
- **WHEN** `invoke(episode)` is called and `episode.isDownloaded` is `true`
- **THEN** `PlaybackController` receives a `PlayableItem` with `mediaUri = localFilePath`.

#### Scenario: Playing a non-downloaded episode
- **WHEN** `invoke(episode)` is called and `episode.isDownloaded` is `false`
- **THEN** `PlaybackController` receives a `PlayableItem` with `mediaUri = episode.audioUrl`.

