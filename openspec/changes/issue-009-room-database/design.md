## Context

Data Schema v1 specifies all entity tables. Room requires Kotlin `@Entity`-annotated data classes and a `@Database` class. This is the foundational persistence layer; everything above it depends on it.

## Goals / Non-Goals

**Goals:**
- All entities from Data Schema v1: `PodcastEntity`, `EpisodeEntity`, `SubscriptionSettingsEntity`, `PlaybackProgressEntity`, `DownloadRecordEntity`, `RadioStationEntity`.
- `AppDatabase` with `exportSchema = true`.
- Version set to `1`.

**Non-Goals:**
- No DAOs yet (ISSUE-010).
- No migration strategies yet (future issue if schema changes).

## Decisions

- Use Room KSP (already in `libs.versions.toml`).
- Entities in `:core:database/entities/` sub-package.
- All columns snake_case matching Data Schema v1 column names.

## Risks / Trade-offs

- **Risk**: Schema drift between Data Schema v1 doc and code.
  - **Mitigation**: Column names MUST match the Data Schema v1 doc exactly; reviewer should cross-check.
