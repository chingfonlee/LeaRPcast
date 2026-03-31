## 1. Bug Triage

- [ ] 1.1 Run manual QA checklist from ISSUE-036; log all failures in a bug table.
- [x] 1.2 Run `./gradlew test` - log all failing tests.
- [x] 1.3 Triage all issues into P1/P2/P3.

## 2. Bug Fixes

- [x] 2.1 Fix all P1 bugs (crashes, data loss, broken happy-path flows).
- [x] 2.2 Fix all P2 bugs (wrong state, incorrect UX behaviour on main flows).
- [x] 2.3 Fix P3 bugs as time allows.
- [x] 2.4 Remove `androidx.work.WorkManagerInitializer` from `androidx.startup.InitializationProvider` so `HiltWorkerFactory` is used for `FeedRefreshWorker` and `AutoCleanupWorker`.

## 3. Final Verification

- [x] 3.1 Run `./gradlew clean :app:assembleDebug` - clean build passes.
- [x] 3.2 Run `./gradlew test` - all tests pass.
- [ ] 3.3 Run QA checklist manually - all scenarios pass.

## 4. Release Notes

- [x] 4.1 Create `docs/RELEASE_NOTES.md` listing MVP features, known limitations, and tested platforms.

## Bug Fix Record

WorkManager startup was still using the default `androidx.startup.InitializationProvider`, so `FeedRefreshWorker` and `AutoCleanupWorker` were created through reflection instead of `HiltWorkerFactory`, causing `NoSuchMethodException` for the expected `(Context, WorkerParameters)` constructor. The fix was to keep `LeaRPcastApplication` as `Configuration.Provider`, remove `androidx.work.WorkManagerInitializer` from the merged startup provider via `tools:node="remove"`, and pin Gradle to Android Studio's bundled JBR in `gradle.properties` so `./gradlew clean :app:assembleDebug` could complete. Verification passed after rebuild, and the merged manifest no longer exposed `WorkManagerInitializer` as an active startup entry.
