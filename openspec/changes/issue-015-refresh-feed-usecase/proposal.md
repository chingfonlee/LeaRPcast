## Why

Fetching and storing RSS feed content must be encapsulated in a use case so feature modules and workers can trigger it without containing data-layer logic themselves.

## What Changes

- Create `RefreshPodcastFeedUseCase` that fetches RSS, parses, compares with DB, and inserts/updates.
- Handle deduplication (no duplicate episodes on repeated refresh).

## Capabilities

### New Capabilities
- `refresh-feed-use-case`: Single entry point for refreshing a podcast's RSS content and syncing it to the local database.

### Modified Capabilities
-

## Impact

- Module: `:domain`
- Unlocks ISSUE-016 (UI triggers refresh), ISSUE-029 (worker triggers refresh).
