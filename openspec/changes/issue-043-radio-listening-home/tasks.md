## 1. Radio Home State

- [x] 1.1 Update `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/model/RadioUiState.kt` to represent the listening-first home states: loading, empty, playing, add-success, playback-failure, and network-failure.
- [x] 1.2 Update `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/viewmodel/RadioViewModel.kt` so the home surface can drive tap-to-play, favorite toggles, add-favorite entry, and retry actions from a single state source.
- [x] 1.3 Verify the view model preserves the last known station list and active playback item when a refresh or error occurs.

## 2. Radio Home UI

- [x] 2.1 Rework `feature/radio/src/main/java/com/learpc/learpc/feature/radio/ui/screen/RadioScreen.kt` into a listening-first layout with a now-playing banner, primary station list, and secondary add-favorite action.
- [x] 2.2 Add or update radio row components so tapping a row starts playback, the play control has the same behavior, and the favorite control is clearly secondary.
- [x] 2.3 Add the empty state, loading state, playback failure state, and network failure state to the same screen without sending the user to a dead end.
- [x] 2.4 Ensure the page layout and touch targets remain comfortable on mobile screens and support one-handed use.

## 3. Strings And Copy

- [x] 3.1 Add the required Radio home strings to `feature/radio/src/main/res/values/strings.xml` for the page title, now-playing banner, empty state, add-favorite action, playback errors, and retry copy.
- [x] 3.2 Replace any hardcoded UI copy in the Radio home surface with `stringResource` lookups.

## 4. Verification

- [x] 4.1 Add or update Compose UI tests for the saved-station list, one-tap playback behavior, favorite toggle, empty state, and error states.
- [x] 4.2 Run the relevant Radio feature tests and a debug build verification to confirm the screen composes and the interactions remain stable.
