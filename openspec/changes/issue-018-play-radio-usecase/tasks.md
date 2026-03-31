## 1. Mapper

- [x] 1.1 Create `RadioPlayableMapper.kt` converting `RadioStation` to `PlayableItem`.

## 2. Use Case

- [x] 2.1 Create `PlayRadioUseCase` in `:domain` injecting `PlaybackController` and `RadioPlayableMapper`.
- [x] 2.2 `operator fun invoke(station: RadioStation)`: map station to `PlayableItem`, call `playbackController.setItem(item)` then `play()`.

## 3. Cleartext Traffic

- [x] 3.1 Create/update `res/xml/network_security_config.xml` in `:app` to allow cleartext for radio stream domains.
- [x] 3.2 Reference it in `:app`'s `AndroidManifest.xml`.

## 4. Verification

- [x] 4.1 Run `./gradlew :domain:assembleDebug`.
