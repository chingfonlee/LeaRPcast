## Context

The `core:media` module needs robust internal abstractions. Directly instantiating `ExoPlayer.Builder` in the `PlaybackService` limits testability and makes it difficult to apply advanced AudioAttributes or caching logic uniformly. The `radio_podcast_technical_design_spec_v_1.md` specifically calls out the need for `PlayerFactory` and core media data models `PlaybackStateModel` / `PlayableItem`. Let's establish these so that cross-module dependencies depend on our pure Kotlin domain models rather than Androidx Media3 objects.

## Goals / Non-Goals

**Goals:**
- Extract player creation into a Hilt-injected `PlayerFactory`.
- Extract session creation into a Hilt-injected `MediaSessionFactory`.
- Define the frozen classes: `PlayableItem`, `PlaybackStateModel`, `PlaybackError`, `InterruptionSnapshot` in `:core:model` (or `:core:media` if preferred, but following TDS rules).

**Non-Goals:**
- Do not implement the `PlaybackController` (StateFlow bridge) yet (that is ISSUE-007).
- Do not add network caching logic to the ExoPlayer yet.

## Decisions

- **Factory Pattern**: Injecting the `ExoPlayer` directly as a Singleton might be problematic if the `PlaybackService` needs to re-initialize or if context scopes differ. Using a `PlayerFactory.create(context)` provides a clean API for the foreground service.
- **Model placement**: Model objects like `PlayableItem` and `PlaybackStateModel` will be placed in `:core:model` so that `:domain` and `:feature:*` modules can consume them without depending strictly on `:core:media`.
- **ExoPlayer Configuration**: `PlayerFactory` will set `setHandleAudioBecomingNoisy(true)` and configure standard `AudioAttributes` for media playback.

## Risks / Trade-offs

- **Risk**: Moving instantiation to a factory implies Hilt must be correctly initialized and `PlaybackService` must inject the factories via `@Inject`.
  - Mitigation: Annotate `PlaybackService` with `@AndroidEntryPoint` and use `@Inject lateinit var playerFactory: PlayerFactory`.
