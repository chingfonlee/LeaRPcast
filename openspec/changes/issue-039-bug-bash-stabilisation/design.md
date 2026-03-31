## Context

Final stabilisation sprint. Aggregate bugs from QA (ISSUE-036) and test failures (ISSUE-037/038). Fix and release.

## Goals / Non-Goals

**Goals:** Bug fixes across modules; `RELEASE_NOTES.md`; final clean build.
**Non-Goals:** New features.

## Decisions

Triage priority: P1 (crash/data loss) → P2 (wrong behaviour on happy path) → P3 (cosmetic). Only P1/P2 are MVP blocking.

## Risks / Trade-offs

- **Risk**: Scope creep into new features during bug bash.
  - **Mitigation**: Strictly no new features; only fix confirmed bugs from QA checklist and test failures.
