## Context

Integration tests verify multi-component data flows: feed refresh inserts episodes, worker chain runs, and progress saves correctly reload. Use Room in-memory DB for isolation.

## Goals / Non-Goals

**Goals:** Integration tests for: feed refresh → DB insertion, auto-download enqueue chain, progress save/reload.
**Non-Goals:** Service-level integration (needs Android instrumented test; defer to future).

## Decisions

Use `Room.inMemoryDatabaseBuilder` in `@Before`. Use real `RefreshPodcastFeedUseCase` with a mock `PodcastRemoteDataSource`.

## Risks / Trade-offs

- **Risk**: Integration tests are slower than unit tests.
  - **Mitigation**: Only cover the three most critical flows; keep each test focused.
