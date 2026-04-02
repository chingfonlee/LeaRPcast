## Why

With data sources in place (ISSUE-013), we can implement repository classes that the domain layer's `UseCase`s will depend on. Repositories orchestrate local/remote sources and expose clean `Flow`-based APIs.

## What Changes

- Implement `DefaultPodcastRepository`, `DefaultEpisodeRepository`, `DefaultSettingsRepository`.
- Bind them to their domain interfaces via `RepositoryModule`.

## Capabilities

### New Capabilities
- `podcast-repositories`: Fully wired repository implementations providing `Flow`-based data access to the domain layer.

### Modified Capabilities
-

## Impact

- Module: `:app/data/repository` or `:core:database` (per Project Structure Spec v1)
- Unlocks ISSUE-015, ISSUE-016, ISSUE-022, ISSUE-023.
