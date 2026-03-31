## Context

Repository implementations bridge data sources to domain interfaces. They orchestrate local vs remote, handle caching, and expose `Flow` streams.

## Goals / Non-Goals

**Goals:** `DefaultPodcastRepository`, `DefaultEpisodeRepository`, `DefaultSettingsRepository` in the repository layer. Bound in `RepositoryModule`.
**Non-Goals:** No use case logic.

## Decisions

Repositories emit from local DB as source of truth; remote fetch only on explicit trigger (use case). `DefaultSettingsRepository` delegates all reads/writes to `UserPreferencesDataSource`.

## Risks / Trade-offs

- **Risk**: Stale local data if remote fetch fails silently.
  - **Mitigation**: Return `Result` from refresh functions; UI shows error if needed.
