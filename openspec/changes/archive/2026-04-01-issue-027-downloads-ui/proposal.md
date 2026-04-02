## Why

Users need to see and manage their downloaded episodes. The Downloads tab (already in nav) needs a real screen showing queued, downloading, and completed items.

## What Changes

- Create `DownloadsScreen` composable.
- Create `DownloadsViewModel` observing `DefaultDownloadRepository`.
- Show download status per episode; add cancel download action.

## Capabilities

### New Capabilities
- `downloads-ui`: Downloads list screen showing download statuses with cancel option.

### Modified Capabilities
-

## Impact

- Module: `:feature:downloads`
- String resources in `:feature:downloads/src/main/res/values/strings.xml`.
