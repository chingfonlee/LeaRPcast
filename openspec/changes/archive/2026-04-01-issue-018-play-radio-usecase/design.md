## Context

`PlayRadioUseCase` is the domain entry point for radio playback. It converts a `RadioStation` model into a `PlayableItem`, sets it on `PlaybackController`, and triggers play.

## Goals / Non-Goals

**Goals:** `PlayRadioUseCase` in `:domain`; `RadioPlayableMapper` converting station → `PlayableItem`; `MediaItem` builder for Media3.
**Non-Goals:** No reconnect logic here (ISSUE-021).

## Decisions

`RadioPlayableMapper` produces a `PlayableItem` with `mediaUri = station.streamUrl` and sets `mediaType = RADIO` (if enum exists in model).

## Risks / Trade-offs

- **Risk**: Station stream URL may be HTTP (unencrypted), blocked by `NetworkSecurityConfig`.
  - **Mitigation**: Add `cleartextTrafficPermitted="true"` for radio domains in `network_security_config.xml` in `:app`.
