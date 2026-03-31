## Why

With data flowing through the repository layer, we can now render the `PodcastListScreen`, `PodcastDetailScreen`, and `EpisodeListScreen`. This is the primary Podcast feature surface delivered at end of Phase 2.

## What Changes

- Create `PodcastListScreen`, `PodcastDetailScreen`, `EpisodeListScreen` composables.
- Create `PodcastViewModel`, `PodcastDetailViewModel`.
- Define `PodcastUiState`, `EpisodeUiState`.
- Wire navigation from `podcast` tab route through to episode list and detail.

## Capabilities

### New Capabilities
- `podcast-ui`: Podcast subscription list, detail, and episode list screens connected to the repository layer.

### Modified Capabilities
-

## Impact

- Module: `:feature:podcast`
- String resources MUST go in `:feature:podcast/src/main/res/values/strings.xml`.
