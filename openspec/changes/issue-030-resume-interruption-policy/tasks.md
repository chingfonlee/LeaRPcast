## 1. Policy Implementation

- [x] 1.1 Create `ResumeAfterInterruptionPolicy.kt` in `:core:media`.
- [x] 1.2 Implement `fun shouldResume(snapshot: InterruptionSnapshot, settings: UserPreferences): Boolean`.
- [x] 1.3 Define `MAX_INTERRUPTION_AGE_MS = 60 * 60 * 1000L`.

## 2. Unit Tests

- [x] 2.1 Test: `resumeAfterCall = false` ??always `false`.
- [x] 2.2 Test: `wasUserPause = true` ??`false`.
- [x] 2.3 Test: snapshot older than threshold ??`false`.
- [x] 2.4 Test: all conditions met ??`true`.

## 3. Verification

- [x] 3.1 Run `./gradlew :core:media:test`.
