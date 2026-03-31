# LeaRPcast v1.0 Release Notes

## MVP Features

- Continuous podcast playback with progress saving and resume handling
- Radio streaming with reconnect logic and retry backoff
- Download queueing, download completion, and cleanup flows
- Audio focus interruption handling and headphone unplug pause behavior
- Settings screen for Wi-Fi only downloads, auto-delete mode, resume-after-call, playback speed, and radio retry settings
- Manual QA checklist and bug report template for bug bash readiness
- Unit and integration coverage for the main data flows

## Stabilisation Summary

- Full Gradle test suite passed in this workspace.
- Clean debug app build passed.
- One low-priority UI warning was fixed in Settings by updating the Material3 dropdown anchor API.

## Known Limitations

- Manual device and emulator QA from ISSUE-036 was not executed in this terminal session.
- Headphone unplug and incoming-call flows still need a human device pass before bug bash sign-off.
- The release is based on automated verification in this workspace, not a full physical-device validation run.

## Tested Platforms

- Windows 11 host
- Android Studio bundled JBR 21
- Gradle debug build: `./gradlew :app:assembleDebug`
- Test suite: `./gradlew test`

## Bug Bash Notes

| Severity | Area | Status | Notes |
| --- | --- | --- | --- |
| P3 | Settings UI | Fixed | Replaced deprecated `Modifier.menuAnchor()` usage with the current Material3 anchor overload to remove a build warning. |
| Info | QA coverage | Open | Manual checklist execution still needs an emulator or physical device pass. |

## Scope Reminder

This release note covers the MVP scope delivered by the current issue set. No new features were added during stabilisation.
