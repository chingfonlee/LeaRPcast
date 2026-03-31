## Why

With data and playback use cases ready, we can render the Radio tab's station list, allow tapping to play, and show basic favourite toggle UI.

## What Changes

- Create `RadioScreen` composable with a station list.
- Create `RadioViewModel` collecting from `DefaultRadioRepository` and calling `PlayRadioUseCase`.

## Capabilities

### New Capabilities
- `radio-ui`: Radio station list screen with tap-to-play and placeholder favourite button.

### Modified Capabilities
-

## Impact

- Module: `:feature:radio`
- String resources in `:feature:radio/src/main/res/values/strings.xml`.
