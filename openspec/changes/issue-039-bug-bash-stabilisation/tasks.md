## 1. Bug Triage

- [ ] 1.1 Run manual QA checklist from ISSUE-036; log all failures in a bug table.
- [x] 1.2 Run `./gradlew test` - log all failing tests.
- [x] 1.3 Triage all issues into P1/P2/P3.

## 2. Bug Fixes

- [x] 2.1 Fix all P1 bugs (crashes, data loss, broken happy-path flows).
- [x] 2.2 Fix all P2 bugs (wrong state, incorrect UX behaviour on main flows).
- [x] 2.3 Fix P3 bugs as time allows.

## 3. Final Verification

- [x] 3.1 Run `./gradlew clean :app:assembleDebug` - clean build passes.
- [x] 3.2 Run `./gradlew test` - all tests pass.
- [ ] 3.3 Run QA checklist manually - all scenarios pass.

## 4. Release Notes

- [x] 4.1 Create `docs/RELEASE_NOTES.md` listing MVP features, known limitations, and tested platforms.
