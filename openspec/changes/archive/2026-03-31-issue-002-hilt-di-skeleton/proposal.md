# ISSUE-002 — 建立 Hilt DI 骨架

## Summary

建立全專案的 Hilt Dependency Injection 入口與基礎 module 骨架。這讓所有後續 ViewModel、Repository、Service 都能透過 DI 注入，而非手動建立物件。

## Phase

Phase 0：專案骨架與基礎設施

## Problem

沒有 Hilt DI 骨架，後續所有需要注入的元件（ViewModel、Repository、ExoPlayer、Room、DataStore）都必須手動建構，無法按照 Clean Architecture 拆分，且難以做單元測試。

## Motivation

- 提供 `@HiltAndroidApp` Application 入口
- 建立基礎 Hilt modules 的 placeholder，供後續 ISSUE 填充
- 確認 Hilt 可正常注入 Activity 與 ViewModel
- 讓 AI Agent 之後的 ISSUE 可以信賴 DI 基礎設施存在

## Proposed Solution

1. 建立 `LeaRPcastApplication` 並加上 `@HiltAndroidApp`
2. 建立 `MainActivity` 並加上 `@AndroidEntryPoint`
3. 建立基礎 Hilt module 骨架（空殼）：
   - `AppModule.kt`
   - `RepositoryModule.kt`
   - `DatabaseModule.kt`
   - `MediaModule.kt`
   - `WorkerModule.kt`

## Impact

- 影響：`:app` module
- 不影響：任何 core/feature module 的業務邏輯

## Dependencies

- Depends on: ISSUE-001

## Non-goals

- 不實作 Repository binding（ISSUE-014+）
- 不實作 Room 注入（ISSUE-009）
- 不實作 ExoPlayer 注入（ISSUE-005）
