# resume-interruption-policy Specification

## Purpose
TBD - created by archiving change issue-030-resume-interruption-policy. Update Purpose after archive.
## Requirements
### Requirement: Resume Policy Decision Function
`ResumeAfterInterruptionPolicy.shouldResume(snapshot, settings)` MUST return `true` only when all conditions (setting enabled, not manual pause, snapshot not too old) are satisfied.

#### Scenario: All conditions met
- **WHEN** `resumeAfterCall = true`, snapshot was not from user pause, and snapshot is less than 60 minutes old
- **THEN** `shouldResume` returns `true`.

#### Scenario: User manually paused
- **WHEN** `snapshot.wasUserPause = true`
- **THEN** `shouldResume` returns `false` regardless of other settings.

