## Why

Beyond unit tests, we need integration tests that verify the end-to-end data flow: feed refresh inserts episodes, auto-download enqueues them, and playback progress is saved and reloaded.

## What Changes

- Write integration tests for: feed refresh → episodes inserted, auto-download evaluation → enqueue, playback progress save → reload correct position.

## Capabilities

### New Capabilities
- `data-flow-integration-tests`: Integration tests covering the full lifecycle from feed fetch through to playback progress.

### Modified Capabilities
-

## Impact

- Modules: `:core:testing`, test source sets of `:domain`, `:core:database`.
