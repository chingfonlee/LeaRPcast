## Why

Rather than every new episode being manually downloaded, the app should evaluate per-podcast and global rules to decide whether to auto-download new episodes when a feed refresh occurs.

## What Changes

- Create `EvaluateAutoDownloadUseCase` applying app-level defaults and per-subscription overrides, respecting Wi-Fi-only settings.

## Capabilities

### New Capabilities
- `auto-download-evaluation`: Rule-based determination of whether a new episode should be automatically downloaded.

### Modified Capabilities
-

## Impact

- Module: `:domain`
- Unlocks ISSUE-029 (background worker uses this).
