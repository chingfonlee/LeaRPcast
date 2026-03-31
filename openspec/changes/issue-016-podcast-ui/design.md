## Context

End of Phase 2. With data flowing, we build the Podcast tab UIs: subscribed podcast list, podcast detail, and episode list. ViewModels collect from repositories.

## Goals / Non-Goals

**Goals:** `PodcastListScreen`, `PodcastDetailScreen`, `EpisodeListScreen`, `PodcastViewModel`, `PodcastDetailViewModel`, `PodcastUiState`, `EpisodeUiState`. Navigation between podcast tab → detail → episodes.
**Non-Goals:** Playback from here (ISSUE-024), adding subscriptions (future).

## Decisions

- `PodcastListScreen` uses LazyColumn.
- Navigate to `PodcastDetailScreen` passing `podcastId` as nav arg.

## Risks / Trade-offs

- **Risk**: Large episode lists may lag.
  - **Mitigation**: Use `LazyColumn` + `key { episode.id }`.
