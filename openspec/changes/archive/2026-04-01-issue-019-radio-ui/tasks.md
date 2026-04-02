## 1. UiState & ViewModel

- [x] 1.1 Create `RadioUiState` (loading, stations list, error).
- [x] 1.2 Create `RadioViewModel` with `@HiltViewModel`, injecting `RadioRepository` and `PlayRadioUseCase`.

## 2. RadioScreen

- [x] 2.1 Create `RadioScreen.kt` with `LazyColumn` of station rows.
- [x] 2.2 Each row: station name, country, play `IconButton`.
- [x] 2.3 Show `CircularProgressIndicator` while loading; error `Text` on failure.

## 3. Strings

- [x] 3.1 Add strings to `:feature:radio/src/main/res/values/strings.xml` (e.g., `radio_loading`, `radio_error`, `radio_play_button`).

## 4. Verification

- [x] 4.1 Run `./gradlew :feature:radio:assembleDebug`.
- [ ] 4.2 Launch app; Radio tab shows station list from cache or network.
