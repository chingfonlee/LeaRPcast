## 1. API Service

- [x] 1.1 Create `RadioBrowserApiService` Retrofit interface with `getTopStations(limit)` endpoint.
- [x] 1.2 Create `RemoteRadioStation` DTO matching RadioBrowser JSON response.
- [x] 1.3 Add service to `NetworkModule`.

## 2. Data Sources

- [x] 2.1 Create `RadioRemoteDataSource` wrapping `RadioBrowserApiService`.
- [x] 2.2 Create `RadioLocalDataSource` wrapping `RadioStationDao`.
- [x] 2.3 Create `RadioStationMapper` (entity to domain).

## 3. Repository

- [x] 3.1 Create `DefaultRadioRepository` implementing `RadioRepository` in `:domain`.
- [x] 3.2 Implement cache-first strategy: emit local, refresh remote asynchronously, upsert results.
- [x] 3.3 Bind in `RepositoryModule`.

## 4. Verification

- [x] 4.1 Run `./gradlew :core:network:assembleDebug`.
