## 1. PlaybackErrorClassifier

- [x] 1.1 Create `PlaybackErrorClassifier.kt` in `:core:media`.
- [x] 1.2 Implement `fun isRetryable(exception: PlaybackException): Boolean` mapping known Media3 error codes.

## 2. RadioReconnectPolicy

- [x] 2.1 Create `RadioReconnectPolicy` data class with `maxAttempts: Int`, `baseDelayMs: Long`, `maxDelayMs: Long`.
- [x] 2.2 Implement `fun nextRetryDelayMs(attempt: Int): Long` with exponential backoff; return `-1L` if `attempt >= maxAttempts`.

## 3. Hilt Binding

- [x] 3.1 Provide `RadioReconnectPolicy` with default config values in `MediaModule`.

## 4. Unit Tests

- [x] 4.1 Test `PlaybackErrorClassifier` for each error code category.
- [x] 4.2 Test `RadioReconnectPolicy` backoff calculation and max attempts boundary.
