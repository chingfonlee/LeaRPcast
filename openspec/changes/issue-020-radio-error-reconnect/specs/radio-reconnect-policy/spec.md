## ADDED Requirements

### Requirement: Error Retryability Classification
`PlaybackErrorClassifier.isRetryable(exception)` MUST return `true` for transient network errors and `false` for permanent source/format errors.

#### Scenario: Transient network error
- **WHEN** ExoPlayer reports `ERROR_CODE_IO_NETWORK_CONNECTION_FAILED`
- **THEN** `isRetryable` returns `true`.

### Requirement: Exponential Backoff
`RadioReconnectPolicy.nextRetryDelayMs(attempt)` MUST return exponentially increasing delays, stopping retries after `maxAttempts`.

#### Scenario: Max retries exceeded
- **WHEN** `attempt >= maxAttempts`
- **THEN** `nextRetryDelayMs` returns `-1` (or throws `MaxRetriesExceededException`) to signal failure.
