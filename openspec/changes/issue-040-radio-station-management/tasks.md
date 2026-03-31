## 1. Domain And Storage

- [x] 1.1 Add radio station management use cases in `domain/src/main/java/com/learpc/learpc/domain/usecase/` for create, update, delete, and reorder flows.
- [x] 1.2 Ensure the radio repository layer preserves `sortOrder` for user-managed stations and keeps custom stations stable across RadioBrowser refreshes.
- [x] 1.3 Add or update unit tests for custom station persistence and ordering behavior.

## 2. Radio UI

- [x] 2.1 Expand `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/model/RadioUiState.kt` and `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/viewmodel/RadioViewModel.kt` to support add/edit/reorder actions and validation state.
- [x] 2.2 Add an add/edit station surface in `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt`.
- [x] 2.3 Add reorder affordances to the station list while keeping station playback actions intact.
- [x] 2.4 Add the required radio management strings in `feature/radio/src/main/res/values/strings.xml`.

## 3. Verification

- [x] 3.1 Verify that a custom station can be created, edited, deleted, reordered, and still appear in the expected position after app relaunch.
- [x] 3.2 Run `./gradlew clean :app:assembleDebug` and the relevant tests to confirm the feature builds cleanly.
