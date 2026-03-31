## Context

Small UX enhancement: add a play button to episode rows in `EpisodeListScreen` and highlight the currently playing episode.

## Goals / Non-Goals

**Goals:** Play action in episode row; playing highlight via border/icon change.
**Non-Goals:** New ViewModel — use `PodcastDetailViewModel` augmented with `currentItem` from `PlaybackController`.

## Decisions

Pass `currentlyPlayingId: String?` into `EpisodeListScreen` as a parameter from the calling ViewModel which combines episode list with `PlaybackController.currentItem`.

## Risks / Trade-offs

- **Risk**: Combining two flows increases ViewModel complexity.
  - **Mitigation**: Use `combine(episodeFlow, currentItemFlow)` in `PodcastDetailViewModel`.
