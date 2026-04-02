## Context

`PlaybackErrorClassifier` determines if a given `PlaybackException` can be retried. `RadioReconnectPolicy` applies exponential backoff using configurable max retries.

## Goals / Non-Goals

**Goals:** `PlaybackErrorClassifier.isRetryable(exception): Boolean`; `RadioReconnectPolicy.nextRetryDelayMs(attempt): Long` with max retry check.
**Non-Goals:** No service integration yet (ISSUE-021).

## Decisions

Retryable: `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED`, `ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT`. Non-retryable: `ERROR_CODE_PARSING_CONTAINER_MALFORMED`, source-level errors.
Backoff: `baseDelayMs * 2^attempt`, capped at `maxDelayMs`.

## Risks / Trade-offs
- **Risk**: ExoPlayer error codes change between Media3 versions.
  - **Mitigation**: Map by error code constant name; add a comment to review on Media3 version bump.
