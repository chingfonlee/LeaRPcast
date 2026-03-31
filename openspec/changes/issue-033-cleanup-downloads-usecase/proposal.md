## Why

Downloaded episodes accumulate. Per-podcast and global cleanup rules determine which completed downloads should be deleted to free storage. `CleanupDownloadsUseCase` encapsulates this decision.

## What Changes

- Create `CleanupDownloadsUseCase` selecting deletion candidates based on played status, age, and per-podcast/global retention rules.
- Perform file deletion and update Room DB.

## Capabilities

### New Capabilities
- `download-cleanup-policy`: Use case selecting and deleting stale downloaded episodes per configured retention rules.

### Modified Capabilities
-

## Impact

- Module: `:domain`
- Unlocks ISSUE-034 (background worker), ISSUE-037 (unit test).
