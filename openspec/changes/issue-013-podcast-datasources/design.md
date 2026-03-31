## Context

Feature modules must never access DAOs or parsers. Data sources provide a clean abstraction: local wraps DAO, remote wraps feeds. Mappers convert between entity/remote DTOs and domain models.

## Goals / Non-Goals

**Goals:** `PodcastLocalDataSource`, `EpisodeLocalDataSource`, `PodcastRemoteDataSource`, entity↔domain mappers.
**Non-Goals:** No repository orchestration here (ISSUE-014).

## Decisions

Mappers are plain functions/objects, not classes, for simplicity and testability.

## Risks / Trade-offs

- **Risk**: Mapper drift from entity changes.
  - **Mitigation**: Mappers are tested in unit tests alongside schema changes.
