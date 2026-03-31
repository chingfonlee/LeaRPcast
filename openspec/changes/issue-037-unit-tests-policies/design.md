## Context

Unit tests for the four pure-function policies and evaluators. Fast, no Android framework dependencies.

## Goals / Non-Goals

**Goals:** Tests for `RadioReconnectPolicy`, `EvaluateAutoDownloadUseCase`, `ResumeAfterInterruptionPolicy`, `CleanupDownloadsUseCase` candidate selection logic.
**Non-Goals:** Service/integration tests (ISSUE-038).

## Decisions

Use JUnit 4 + MockK (already in `libs.versions.toml`). Run in `:core:media:test` and `:domain:test`.

## Risks / Trade-offs

- **Risk**: Mock drift if interfaces change.
  - **Mitigation**: Keep mocks minimal; focus on boundary and pairwise conditions.
