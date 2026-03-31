## Why

Users need to tap an episode in the list to start playing it. This connects the Podcast UI (ISSUE-016) to `PlayEpisodeUseCase` (ISSUE-022).

## What Changes

- Add play action to episode row in `EpisodeListScreen`.
- Show currently playing episode highlighted.

## Capabilities

### New Capabilities
- `episode-play-entry-ui`: Tap-to-play episode from episode list with active-playing highlight.

### Modified Capabilities
-

## Impact

- Module: `:feature:podcast`
- No new string files — uses existing `:feature:podcast` strings.xml.
