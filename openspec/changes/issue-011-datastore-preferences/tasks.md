## 1. Dependencies

- [x] 1.1 Verify `datastore-preferences` in `:core:datastore` `build.gradle.kts`.

## 2. PreferenceKeys

- [x] 2.1 Create `PreferenceKeys.kt` listing all keys from Data Schema v1 (`WIFI_ONLY_DOWNLOAD`, `AUTO_DELETE_MODE`, `RESUME_AFTER_CALL`, `DEFAULT_PLAYBACK_SPEED`, `RADIO_RETRY_ENABLED`, `RADIO_MAX_RETRIES`).

## 3. UserPreferences & DataSource

- [x] 3.1 Create `UserPreferences` data class with all settings fields and default values.
- [x] 3.2 Create `UserPreferencesDataSource` with `observePreferences(): Flow<UserPreferences>`.
- [x] 3.3 Add suspend update functions: `setWifiOnlyDownload`, `setAutoDeleteMode`, `setResumeAfterCall`, `setDefaultPlaybackSpeed`, `setRadioRetryEnabled`, `setRadioMaxRetries`.

## 4. Hilt Binding

- [x] 4.1 Provide `UserPreferencesDataSource` in a new `DatastoreModule` in `:core:datastore`.

## 5. Verification

- [x] 5.1 Run `./gradlew :core:datastore:assembleDebug`.
