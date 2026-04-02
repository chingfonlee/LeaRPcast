## 1. SettingsViewModel

- [x] 1.1 Create `SettingsViewModel` with `@HiltViewModel`, injecting `SettingsRepository`.
- [x] 1.2 Expose `settingsState: StateFlow<UserPreferences>`.
- [x] 1.3 Add update functions for each setting.

## 2. SettingsScreen

- [x] 2.1 Create `SettingsScreen.kt` replacing placeholder.
- [x] 2.2 Use `LazyColumn` with setting rows: label + `Switch` or `Slider`.
- [x] 2.3 Wire each toggle to corresponding `SettingsViewModel.setX(value)` call.

## 3. Strings

- [x] 3.1 Add all settings string keys to `:feature:settings/src/main/res/values/strings.xml`.

## 4. Verification

- [x] 4.1 Run `./gradlew :feature:settings:assembleDebug`.
- [ ] 4.2 Toggle a setting ??kill app ??relaunch ??confirm setting persisted.
