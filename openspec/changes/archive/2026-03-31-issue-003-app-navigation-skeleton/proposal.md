# ISSUE-003 - Proposal: App Navigation Skeleton

## Summary
建立 app-level Compose Navigation 骨架，讓主要頁面可切換，並為後續 feature 提供路由接入點。placeholder screen 保留在各 feature module，而對應的 placeholder 字串也由各自 feature module 持有。

## Phase
Phase 0

## Problem
目前尚未有 Navigation 骨架，無法把 Radio / Podcast / Downloads / Settings 與後續 feature screen 組裝成完整 App。

## Motivation
- 提供 `AppNavHost` 與 route constants
- 提供底部導覽列，讓使用者能在 Radio / Podcast / Downloads / Settings 間切換
- 讓後續 feature screen 有清楚的接入方式

## Proposed Solution
1. 建立 `AppDestinations.kt` 定義 route constants
2. 建立 `AppNavHost.kt` 負責組裝 destinations
3. 建立底部導覽列與 tab metadata
4. 各 feature 建立空白 placeholder screen composable，並由各自 feature module 持有對應的 placeholder 字串

## Impact
- 影響：`:app` module（navigation/）、各 feature module（placeholder screen 與各自的 strings.xml）
- 不影響：既有 core / domain 架構

## Dependencies
- Depends on: ISSUE-001, ISSUE-002

## Non-goals
- 不實作 feature 的完整 UI（各 feature ISSUE 負責）
- 不實作 Player 的完整播放 UI（由後續 ISSUE 負責）
- 不做 deep link 與通知捷徑
