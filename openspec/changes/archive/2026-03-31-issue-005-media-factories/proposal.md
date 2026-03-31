# ISSUE-005 — Proposal: core:media models and factories

## Why

In ISSUE-004, we successfully built the `PlaybackService` and initial Media3 integration. However, in order for the domain and feature modules to safely interact with our media core, we need to extract the direct instantiation of `ExoPlayer` and `MediaSession` into testable factories. Additionally, we need to establish the core domain-level models (like `PlayableItem`, `PlaybackStateModel`, and `PlaybackError`) without tightly coupling them to ExoPlayer internals, ensuring our UI layer remains agnostic of Media3 details.

## What Changes

- Abstract `ExoPlayer` instantiation into a `PlayerFactory`.
- Abstract `MediaSession` creation into a `MediaSessionFactory`.
- Create core media models: `PlayableItem`, `PlaybackStateModel`, `PlaybackError`, and `InterruptionSnapshot`.
- Inject these factories into `PlaybackService` to decouple its dependencies.

## Capabilities

### New Capabilities
- `media-model-factory`: Establish `PlayerFactory`, `MediaSessionFactory`, and core playback state models, providing an abstraction layer for media operations.

### Modified Capabilities
- 

## Impact

- Impacted Modules: `:core:media`, `:core:model`
- This change unlocks the ability for components to subscribe to a purely domain-driven state, paving the way for the `PlaybackController` (ISSUE-007).
