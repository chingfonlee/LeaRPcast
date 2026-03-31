## Context

Phase 1 core work. `PlaybackService` exists as a skeleton. We now add Hilt injection, foreground notification (via Media3's built-in support), and the `SessionToken`/`MediaController` pathway. Media3 `MediaSessionService` handles notification automatically when a `MediaSession` is attached.

## Goals / Non-Goals

**Goals:**
- Annotate `PlaybackService` with `@AndroidEntryPoint`.
- Inject `PlayerFactory` and `MediaSessionFactory` (from ISSUE-005).
- Wire Media3 automatic notification by calling `setMediaNotificationProvider` or rely on default `DefaultMediaNotificationProvider`.
- Provide a `SessionToken` from `MediaModule` so `MainActivity` can build a `MediaController`.

**Non-Goals:**
- No custom notification styling yet (Phase 6).
- No reconnect logic (ISSUE-021).
- No audio focus management yet (ISSUE-031).

## Decisions

- Use Media3's built-in automatic notification management to avoid boilerplate and stay aligned with its lifecycle recommendations.
- `SessionToken` is a singleton provided by Hilt `MediaModule` (already done in ISSUE-004/005).

## Risks / Trade-offs

- **Risk**: `MediaController.Builder` in `MainActivity` must be created and released with the Activity lifecycle.
  - **Mitigation**: Use `ListenableFuture` and `controllerFuture.cancel(true)` in `onDestroy`.
