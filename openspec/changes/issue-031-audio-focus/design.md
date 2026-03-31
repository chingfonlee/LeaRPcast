## Context

Audio focus integration ensures polite playback behaviour. ExoPlayer handles focus automatically when `handleAudioFocus = true`; we layer snapshots and policy evaluation on top.

## Goals / Non-Goals

**Goals:** Verify `PlayerFactory` sets `handleAudioFocus = true`. Add snapshot save on focus loss; query policy on focus gain; resume conditionally.
**Non-Goals:** UI indicator (future).

## Decisions

Leverage ExoPlayer's built-in focus handling for the underlying ducking/pausing; we only add the snapshot + policy layer on top via `Player.Listener.onIsPlayingChanged`.

## Risks / Trade-offs

- **Risk**: Double-pause if ExoPlayer handles focus AND we pause again.
  - **Mitigation**: Check `player.isPlaying` before calling `player.play()` on focus gain.
