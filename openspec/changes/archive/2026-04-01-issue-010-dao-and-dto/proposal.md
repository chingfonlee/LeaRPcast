## Why

With the Room schema in place (ISSUE-009), we need DAOs to query and mutate data, and DTOs for complex relation aggregates. Without these, feature modules and repositories have no data access layer.

## What Changes

- Create DAO interfaces for each entity group.
- Create relation data classes (e.g., `PodcastWithEpisodes`).
- Update `DatabaseModule` to provide DAOs via DI.

## Capabilities

### New Capabilities
- `room-dao-layer`: DAO interfaces and relation DTOs providing a clean data access boundary.

### Modified Capabilities
-

## Impact

- Module: `:core:database`
- Unlocks ISSUE-013 (data sources) and all repository implementations.
