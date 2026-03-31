# ISSUE-001 — 建立 Gradle Modules 與基本專案骨架

## Summary

建立 LeaRPcast 的 multi-module Android 專案骨架。這是所有後續開發任務的基礎，沒有正確的 module 結構，其他 ISSUE 無法正確進行。

## Phase

Phase 0：專案骨架與基礎設施

## Problem

全新 Android Studio 專案預設只有單一 `:app` module。根據 Project Structure Spec v1，本專案需要 14 個 Gradle modules，按照 feature-first + layered 的 Clean Architecture 分工。若不在最早期建立正確的 module 結構，後期修正會帶來大量相依衝突。

## Motivation

- AI Agent 需要清楚的 module 邊界來分工而不打架
- 避免 DAO/Entity 被誤放進 feature module
- 提早確認 Gradle sync 正常，降低後期基礎設施風險
- 提供後續所有 ISSUE 的實際 package 結構地基

## Proposed Solution

依 Project Structure Spec v1 建立以下 Gradle modules，每個 module 只建立最小可編譯骨架（空 package + placeholder file）：

- `:app`
- `:core:common`
- `:core:ui`
- `:core:network`
- `:core:database`
- `:core:datastore`
- `:core:media`
- `:core:model`
- `:core:testing`
- `:domain`
- `:feature:radio`
- `:feature:podcast`
- `:feature:player`
- `:feature:downloads`
- `:feature:settings`

## Impact

- 影響：`settings.gradle.kts`、所有 `build.gradle.kts`
- 不影響：任何業務邏輯、UI、資料庫

## Dependencies

- Depends on: 無（這是 Phase 0 的第一個任務）

## Non-goals

- 不實作任何播放、資料、UI 邏輯
- 不設定 Hilt（由 ISSUE-002 處理）
- 不設定 Navigation（由 ISSUE-003 處理）
