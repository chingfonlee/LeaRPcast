## 1. RadioReconnectPolicy Tests

- [x] 1.1 Test: `nextRetryDelayMs(0)` ??`baseDelayMs`.
- [x] 1.2 Test: delay doubles on each attempt.
- [x] 1.3 Test: `nextRetryDelayMs(maxAttempts)` returns `-1L`.

## 2. EvaluateAutoDownloadUseCase Tests

- [x] 2.1 Test: `wifiOnly=true` + metered network ??`false`.
- [x] 2.2 Test: per-podcast override disabled ??`false`.
- [x] 2.3 Test: all conditions met ??`true`.

## 3. ResumeAfterInterruptionPolicy Tests

- [x] 3.1 Test: user paused snapshot ??`false`.
- [x] 3.2 Test: snapshot too old ??`false`.
- [x] 3.3 Test: setting disabled ??`false`.
- [x] 3.4 Test: all conditions met ??`true`.

## 4. CleanupDownloadsUseCase Tests (candidate selection only)

- [x] 4.1 Test: currently playing episode is excluded.
- [x] 4.2 Test: `isMarkedForKeep = true` episode is excluded.
- [x] 4.3 Test: within retention period is excluded.

## 5. Verification

- [x] 5.1 Run `./gradlew :core:media:test :domain:test` ??all tests green.
