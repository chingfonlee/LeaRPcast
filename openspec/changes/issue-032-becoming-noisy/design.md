## Context

Headphone unplug should auto-pause. This is already handled by `setHandleAudioBecomingNoisy(true)` in ExoPlayer. This issue verifies and tests it explicitly.

## Goals / Non-Goals

**Goals:** Verify `handleAudioBecomingNoisy = true` active; manual device test; add BroadcastReceiver only if ExoPlayer's handling proves insufficient in testing.
**Non-Goals:** Custom receiver if ExoPlayer handles it correctly.

## Decisions

If ExoPlayer's built-in handling works (it should with `handleAudioBecomingNoisy`), no additional code is needed. This issue is primarily a verification + test milestone.

## Risks / Trade-offs

- **Risk**: Some OEM modifications suppress `ACTION_AUDIO_BECOMING_NOISY`.
  - **Mitigation**: Fallback: add explicit `BroadcastReceiver` if OEM-specific issues found during QA.
