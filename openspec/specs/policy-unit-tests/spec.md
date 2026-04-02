# policy-unit-tests Specification

## Purpose
TBD - created by archiving change issue-037-unit-tests-policies. Update Purpose after archive.
## Requirements
### Requirement: Policy Unit Tests All Pass
All unit tests for core policy classes MUST pass with `./gradlew test`.

#### Scenario: Running all policy tests
- **WHEN** `./gradlew :core:media:test :domain:test` is executed
- **THEN** all tests in `RadioReconnectPolicyTest`, `EvaluateAutoDownloadUseCaseTest`, `ResumeAfterInterruptionPolicyTest`, and `CleanupDownloadsUseCaseTest` pass with no failures.

