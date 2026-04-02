## Context

Auto-download needs a rule evaluator. `EvaluateAutoDownloadUseCase` takes a new episode and outputs `shouldDownload: Boolean` based on app and per-subscription settings.

## Goals / Non-Goals

**Goals:** Pure decision function with signature `suspend operator fun invoke(episode: Episode, podcastId: String): Boolean`. Considers: `globalAutoDownload`, `perPodcastAutoDownload` override, `wifiOnly + isOnWifi`, `maxEpisodesBeforeDelete`.
**Non-Goals:** Download trigger (caller's responsibility).

## Decisions

Check connectivity inline via `ConnectivityManager`; pass as injected `NetworkChecker` abstraction for testability.

## Risks / Trade-offs

- **Risk**: Complex rule combinations lead to hard-to-read logic.
  - **Mitigation**: Encapsulate each condition in a named local variable with a comment.
