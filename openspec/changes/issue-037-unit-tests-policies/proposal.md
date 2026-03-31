## Why

Core policies (`RadioReconnectPolicy`, `EvaluateAutoDownloadUseCase`, `ResumeAfterInterruptionPolicy`, `CleanupDownloadsUseCase`) are pure functions and excellent candidates for comprehensive unit testing.

## What Changes

- Write unit tests for each policy class using standard JUnit + MockK.
- Cover happy paths, edge cases, and boundary conditions for each policy.

## Capabilities

### New Capabilities
- `policy-unit-tests`: Unit test coverage for all core policy and evaluator classes.

### Modified Capabilities
-

## Impact

- Module: `:core:testing` or test source sets of the owning modules.
