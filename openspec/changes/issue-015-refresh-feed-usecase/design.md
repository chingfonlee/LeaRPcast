## Context

`RefreshPodcastFeedUseCase` is the single entry point for syncing remote RSS content to local DB. It orchestrates: fetch → parse → diff → upsert. Deduplication is via episode GUID.

## Goals / Non-Goals

**Goals:** Full feed refresh flow in domain layer.
**Non-Goals:** Auto-download evaluation (ISSUE-028), UI polling (ISSUE-016).

## Decisions

- Deduplicate episodes by `guid` field.
- Use `upsert` semantic — update existing, insert new; never delete (deletion is a separate cleanup concern).

## Risks / Trade-offs

- **Risk**: Very large feeds with 500+ episodes could be slow.
  - **Mitigation**: Accept as-is for MVP; optimise later with incremental fetch if needed.
