## 1. Worker

- [x] 1.1 Create `AutoCleanupWorker` extending `CoroutineWorker` in `:app/work`, annotated with `@HiltWorker`.
- [x] 1.2 `@Inject` `CleanupDownloadsUseCase`.
- [x] 1.3 In `doWork()`: call `cleanupDownloadsUseCase.invoke()` → log result → `return Result.success()`.

## 2. Scheduling

- [x] 2.1 In `LeaRPcastApplication.onCreate`, enqueue daily `PeriodicWorkRequest` with `BatteryNotLow` constraint.
- [x] 2.2 Use `ExistingPeriodicWorkPolicy.KEEP`.

## 3. Verification

- [x] 3.1 Run `./gradlew :app:assembleDebug`.
- [ ] 3.2 Manually trigger one-time `AutoCleanupWorker` and verify deleted files and DB state.
