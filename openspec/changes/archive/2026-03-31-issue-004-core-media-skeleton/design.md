## Context

The App Navigation and Gradle Module skeleton (Phase 0) is complete. The application now needs a robust media playback core. According to the `radio_podcast_technical_design_spec_v_1.md` and project architecture rules, we must implement Jetpack Media3 (`ExoPlayer` + `MediaSessionService`) as the central playback engine. This core module (`:core:media`) will be responsible for handling all audio playing logic, background playback, and exposing a `MediaController` for the UI to observe and control state.

## Goals / Non-Goals

**Goals:**
- Establish a `PlaybackService` extending Media3's `MediaSessionService`.
- Provide a single `ExoPlayer` instance and a Jetpack `MediaSession`.
- Expose basic dependency injection bindings for Media3 components so they can be provided to other modules via Hilt.
- Define basic Media3 service declarations in the AndroidManifest.xml.

**Non-Goals:**
- Do not implement custom logic for fetching radio streams or parsing Podcast RSS feeds (these belong to feature modules and `:core:network`).
- Do not build any playback UI controls (this goes to `:feature:player`).
- Do not implement downloading capabilities yet.

## Decisions

- **Single ExoPlayer Instance**: Adhering to the architecture rules, a single instance of `ExoPlayer.Builder(context).build()` will be created and hosted in `PlaybackService`.
- **Hilt Integration**: We will define an `@Module @InstallIn(SingletonComponent::class)` inside `:core:media` to provide `ExoPlayer` and `MediaSession` dependencies. This makes it trivial for view models or the `MediaController` service connector to inject required instances.
- **Service Registration**: The `PlaybackService` will be registered in `:core:media`'s `AndroidManifest.xml` with the intent filter `androidx.media3.session.MediaSessionService` to allow system components to connect to it.

## Risks / Trade-offs

- **Risk**: Memory leaks if ExoPlayer isn't released properly.
  - Mitigation: Ensure `player.release()`, `mediaSession.release()` and proper lifecycle cleanup in `onDestroy()` of `PlaybackService`.
- **Risk**: Missing permissions for Foreground Service (Playback).
  - Mitigation: Declare `FOREGROUND_SERVICE` and `FOREGROUND_SERVICE_MEDIA_PLAYBACK` in AndroidManifest for Android 14+ support.
