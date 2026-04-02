## Why

`PlaybackService` is the backbone of audio playback. With models and factories in place (ISSUE-005), we now need to wire the service fully: connect `@AndroidEntryPoint` DI, expose foreground notification for background playback, and provide a stable `MediaController` connection point so that the UI layer and domain layer can observe and control playback. Without this, no feature module can play any audio.

## What Changes

- Upgrade `PlaybackService` to use Hilt-injected factories.
- Wire foreground notification via Media3's built-in `MediaNotificationManager` or a custom notification channel.
- Expose the `SessionToken` via `MediaController.Builder` in the app layer so the UI can connect.

## Capabilities

### New Capabilities
- `playback-service-lifecycle`: A fully wired `PlaybackService` with foreground notification, Hilt DI, and an external `MediaController` binding point for UI modules.

### Modified Capabilities
-

## Impact

- Impacted Modules: `:core:media`, `:app` (for MediaController wiring in `MainActivity`)
- ISSUE-006 unblocks ISSUE-007 (PlaybackController / StateFlow bridge).
