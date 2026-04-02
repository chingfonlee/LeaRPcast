## Context

After focus is regained, `ResumeAfterInterruptionPolicy` decides whether to resume based on snapshot age, pause reason, and settings.

## Goals / Non-Goals

**Goals:** `ResumeAfterInterruptionPolicy` data class with `fun shouldResume(snapshot: InterruptionSnapshot, settings: UserPreferences): Boolean`. Pure function; no side effects.
**Non-Goals:** Actual focus handling (ISSUE-031).

## Decisions

Conditions for `shouldResume = true`: `settings.resumeAfterCall == true`, snapshot was NOT caused by user manual pause, snapshot age < threshold (e.g., 60 minutes).

## Risks / Trade-offs

- **Risk**: Clock-based threshold is fragile for DST changes.
  - **Mitigation**: Use `System.currentTimeMillis()` difference; DST impact is at most 1h which is within margin.
