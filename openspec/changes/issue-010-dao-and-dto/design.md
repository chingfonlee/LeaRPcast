## Context

Entities are defined. Now we add `@Dao` interfaces for each entity group and relation DTOs that aggregate joined queries. `DatabaseModule` will expose these DAOs for injection.

## Goals / Non-Goals

**Goals:**
- `PodcastDao`, `EpisodeDao`, `PlaybackProgressDao`, `DownloadRecordDao`, `RadioStationDao`.
- Relation DTO: `PodcastWithEpisodes`, `PodcastWithSubscriptionSettings`.
- `DatabaseModule` provides all DAOs from `AppDatabase`.

**Non-Goals:**
- No repository logic here — DAOs are pure DB access.

## Decisions

- DAOs use `Flow` for queries so callers get reactive updates.
- Upsert via `@Insert(onConflict = OnConflictStrategy.REPLACE)`.

## Risks / Trade-offs

- **Risk**: N+1 query issues if relation DTOs are misused.
  - **Mitigation**: Use `@Relation` + `@Transaction` for aggregate queries.
