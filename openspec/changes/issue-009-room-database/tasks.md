## 1. Add Room Dependencies

- [x] 1.1 Verify `room-runtime`, `room-ktx`, `room-compiler` are in `libs.versions.toml` (done in ISSUE-001; confirm version matches).
- [x] 1.2 Add Room dependencies to `:core:database` `build.gradle.kts`.
- [x] 1.3 Add KSP plugin and Room plugin to `:core:database` `build.gradle.kts`.

## 2. Create Entity Classes

- [x] 2.1 Create `PodcastEntity.kt` in `core/database/src/main/java/.../entities/`.
- [x] 2.2 Create `EpisodeEntity.kt`.
- [x] 2.3 Create `SubscriptionSettingsEntity.kt`.
- [x] 2.4 Create `PlaybackProgressEntity.kt`.
- [x] 2.5 Create `DownloadRecordEntity.kt`.
- [x] 2.6 Create `RadioStationEntity.kt`.

## 3. Create AppDatabase

- [x] 3.1 Create `AppDatabase.kt` annotated with `@Database(entities = [...], version = 1, exportSchema = true)`.
- [x] 3.2 Set export schema directory in `build.gradle.kts` via `room { schemaDirectory(...) }`.

## 4. Hilt Module

- [x] 4.1 Update `DatabaseModule` to provide `AppDatabase` via `Room.databaseBuilder(...)` as `@Singleton`.

## 5. Verification

- [x] 5.1 Run `./gradlew :core:database:kspDebugKotlin` ??Room code generation should succeed.
- [x] 5.2 Confirm schema JSON is exported to the configured directory.
