## 1. NetworkChecker Abstraction

- [x] 1.1 Create `NetworkChecker` interface with `fun isOnUnmeteredNetwork(): Boolean` in `:core:common`.
- [x] 1.2 Create `ConnectivityManagerNetworkChecker` implementation using `ConnectivityManager`.
- [x] 1.3 Bind via Hilt in `AppModule`.

## 2. Use Case

- [x] 2.1 Create `EvaluateAutoDownloadUseCase` in `:domain` injecting `SettingsRepository`, `PodcastRepository`, and `NetworkChecker`.
- [x] 2.2 Implement rule evaluation logic (global default → podcast override → Wi-Fi condition).

## 3. Unit Tests

- [x] 3.1 Test all rule combinations with mock `SettingsRepository` and mock `NetworkChecker`.

## 4. Verification

- [x] 4.1 Run `./gradlew :domain:test`.
