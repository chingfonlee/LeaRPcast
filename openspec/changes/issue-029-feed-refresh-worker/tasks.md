## 1. Worker

- [x] 1.1 Create `FeedRefreshWorker` extending `CoroutineWorker` in `:app/work`, annotated with `@HiltWorker`.
- [x] 1.2 `@Inject` `PodcastRepository`, `RefreshPodcastFeedUseCase`, `EvaluateAutoDownloadUseCase`, `EnqueueEpisodeDownloadUseCase`.
- [x] 1.3 In `doWork()`: iterate subscribed podcasts, refresh each, collect new episodes, evaluate, enqueue if qualifying.
- [x] 1.4 Return `Result.success()` on completion, `Result.retry()` on network failure.

## 2. Scheduling

- [x] 2.1 In `LeaRPcastApplication.onCreate`, enqueue `PeriodicWorkRequest` with 4h interval and `NetworkType.CONNECTED` constraint.
- [x] 2.2 Use `ExistingPeriodicWorkPolicy.KEEP` to avoid duplicate scheduling.

## 3. Verification

- [x] 3.1 Run `./gradlew :app:assembleDebug` to verify worker injection and app startup wiring.
- [ ] 3.2 Manually trigger one-time work request and verify podcast DB updates.
