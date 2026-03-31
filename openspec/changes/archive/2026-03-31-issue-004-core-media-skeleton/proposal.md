# ISSUE-004 — Proposal: core:media skeleton

## Why

Phase 0 setup is complete, and the project is now ready for Phase 1. The primary architectural requirement of the LeaRPcast app is to possess a robust media player that supports audio playback (radio and podcasts) in the background. To achieve this, it is necessary to establish the `core:media` module skeleton, implementing Jetpack Media3's `ExoPlayer`, `MediaSession`, and `MediaSessionService`. Establishing this foundation early helps ensure that subsequent modules (like the player UI) can directly integrate with a stable playback abstraction.

## What Changes

- Initialize the `core:media` module with necessary Jetpack Media3 dependencies (if not fully defined).
- Set up a standard Jetpack Media3 `MediaSessionService` (`PlaybackService`).
- Provide an `ExoPlayer` instance and a `MediaSession` within the service.
- Implement the core dependency injection logic to provide these media endpoints securely across the app.
- Provide a `MediaController` hook for UI modules to interact with playback states.

## Capabilities

### New Capabilities
- `media-playback-service`: Establish the background playback service structure using Jetpack Media3 components (`MediaSessionService`, `ExoPlayer`, `MediaSession`).

### Modified Capabilities
- 

## Impact

- Impacted Modules: `:core:media`, `:app` (for configuring service manifest or DI mapping).
- Code Constraints: Adheres to the "Single ExoPlayer instance in PlaybackService" architecture rule explicitly.
