# Issue List v1

## 專案名稱
Android 廣播與 Podcast 收聽 App

## 文件版本
v1.0

## 文件目的
本文件將既有 PRD、Technical Design Spec、Data Schema、Project Structure Spec 拆解為可執行的開發任務。每個任務都可直接轉為：
- GitHub Issue
- AI Agent 工作單
- Sprint backlog item

本文件設計原則：
- 每個 task 要有明確邊界
- 每個 task 要能單獨驗收
- task 之間相依關係清楚
- 避免「一張單做完整個 App」

---

# 1. 開發階段總覽

建議分成 7 個階段：

## Phase 0：專案骨架與基礎設施
建立 module、DI、導航、基礎設定。

## Phase 1：播放核心骨架
建立 MediaSessionService、ExoPlayer、播放狀態流。

## Phase 2：Podcast 資料流
建立 Room / DataStore、RSS 解析、Podcast 與 Episode 查詢流程。

## Phase 3：Radio 功能
建立電台列表、Radio 播放與重連。

## Phase 4：Podcast 播放與下載
建立 episode 播放、下載、離線播放與自動下載。

## Phase 5：中斷恢復與清理規則
建立來電中斷、耳機拔除、自動恢復與自動刪除。

## Phase 6：設定、QA 與收尾
建立設定頁、手動驗收、bug 修補與穩定性打磨。

---

# 2. 任務狀態欄位建議

每張 issue 建議使用以下欄位：
- **Issue ID**
- **Title**
- **Goal**
- **Why**
- **Depends on**
- **Input docs**
- **Scope**
- **Out of scope**
- **Deliverables**
- **Acceptance Criteria**
- **Notes / Risks**

---

# 3. Phase 0：專案骨架與基礎設施

## ISSUE-001 建立 Gradle modules 與基本專案骨架
### Goal
建立 Project Structure Spec v1 所定義的多 module 專案骨架。

### Depends on
- 無

### Input docs
- Project Structure Spec v1

### Scope
- 建立所有 Gradle modules
- 配置 module 相依
- 建立 base package 結構
- 建立空的 README / placeholder packages

### Out of scope
- 不實作任何播放或資料邏輯

### Deliverables
- modules 建立完成
- settings.gradle.kts / build.gradle.kts 更新
- 每個 module 至少有最小可編譯骨架

### Acceptance Criteria
- 專案可成功 sync
- 所有 module 可編譯
- package 結構符合文件

---

## ISSUE-002 建立 Hilt DI 骨架
### Goal
建立全專案 DI 入口與基礎 modules。

### Depends on
- ISSUE-001

### Input docs
- Project Structure Spec v1
- Technical Design Spec v1

### Scope
- Hilt plugin 與設定
- Application class
- AppModule / RepositoryModule / DatabaseModule / MediaModule placeholder

### Deliverables
- `RadioCastApplication`
- 基本 Hilt module
- Hilt 可注入 Activity / ViewModel

### Acceptance Criteria
- App 可啟動
- Hilt 注入正常
- 無循環相依

---

## ISSUE-003 建立 App Navigation 骨架
### Goal
建立 app-level navigation host 與主要 destinations。

### Depends on
- ISSUE-001
- ISSUE-002

### Input docs
- Project Structure Spec v1
- PRD

### Scope
- AppNavHost
- 底部頁籤或主頁入口
- Radio / Podcast / Downloads / Settings / Player route placeholders

### Deliverables
- 可切換的空白頁面骨架
- route constants

### Acceptance Criteria
- App 可在各主要頁切換
- 無 feature 與 app 導航耦合錯亂

---

# 4. Phase 1：播放核心骨架

## ISSUE-004 建立 core:media 播放模型與錯誤模型
### Goal
建立 PlayableItem、PlaybackStateModel、PlaybackError、InterruptionSnapshot 等核心資料結構。

### Depends on
- ISSUE-001

### Input docs
- Technical Design Spec v1

### Scope
- model classes
- sealed interfaces / enums
- 基本 error classifier skeleton

### Deliverables
- `PlayableItem.kt`
- `PlaybackStateModel.kt`
- `PlaybackError.kt`
- `InterruptionSnapshot.kt`

### Acceptance Criteria
- 模型可被其他 module 引用
- 狀態命名與 TDS 一致

---

## ISSUE-005 建立 ExoPlayer Factory 與 MediaSession Factory
### Goal
建立播放器與媒體 session 的工廠封裝。

### Depends on
- ISSUE-004
- ISSUE-002

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- PlayerFactory
- MediaSessionFactory
- audio attributes 基本設定

### Deliverables
- `PlayerFactory.kt`
- `MediaSessionFactory.kt`
- `MediaModule.kt` 更新

### Acceptance Criteria
- 可透過 DI 取得 ExoPlayer
- 可建立 MediaSession
- 不在 feature module 直接 new player

---

## ISSUE-006 建立 PlaybackService 骨架
### Goal
建立 MediaSessionService 作為播放核心服務。

### Depends on
- ISSUE-005
- ISSUE-003

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- PlaybackService
- service lifecycle
- foreground notification 基本 wiring
- 對外暴露 MediaController 連線能力

### Deliverables
- `PlaybackService.kt`
- service manifest wiring
- service 啟動與綁定流程

### Acceptance Criteria
- App 啟動後可連上 service
- service 可持有單一 player instance
- 通知可出現基本播放狀態

---

## ISSUE-007 建立 PlaybackController 與狀態同步橋接
### Goal
讓 UI 可觀察 PlaybackService 的播放狀態。

### Depends on
- ISSUE-006
- ISSUE-004

### Input docs
- Technical Design Spec v1

### Scope
- StateFlow bridge
- player event -> PlaybackStateModel mapper
- UI 端可 collect state

### Deliverables
- `PlaybackController.kt`
- `PlayerEventMapper.kt`
- 基本狀態流 API

### Acceptance Criteria
- UI 可看到 idle/loading/playing/paused 狀態切換
- 狀態來源單一且可觀察

---

## ISSUE-008 建立 feature:player 基本 Player UI
### Goal
建立 full player 與 mini player 基本 UI。

### Depends on
- ISSUE-007
- ISSUE-003

### Input docs
- PRD
- Project Structure Spec v1

### Scope
- PlayerScreen
- MiniPlayer
- 基本播放/暫停按鈕
- 顯示標題、subtitle、artwork placeholder

### Deliverables
- PlayerScreen
- PlayerViewModel
- PlayerUiState / Event

### Acceptance Criteria
- 能顯示目前播放項目資訊
- 可進行播放/暫停控制

---

# 5. Phase 2：Podcast 資料流

## ISSUE-009 建立 Room Database 與 Entities
### Goal
依 Data Schema v1 建立 Room database 與全部必要 entity。

### Depends on
- ISSUE-001

### Input docs
- Data Schema v1

### Scope
- AppDatabase
- podcast/episode/subscription/playback/download/radio entities
- Room annotations

### Deliverables
- `AppDatabase.kt`
- entities 檔案

### Acceptance Criteria
- Room schema 可編譯
- exportSchema 正常
- 欄位與命名符合 schema 文件

---

## ISSUE-010 建立 DAO 與 Relation DTO
### Goal
建立 DAO 與 relation aggregates。

### Depends on
- ISSUE-009

### Input docs
- Data Schema v1

### Scope
- 各 DAO 基礎 CRUD / query
- relation DTO
- DatabaseModule wiring

### Deliverables
- DAO 介面
- relation classes

### Acceptance Criteria
- 常見查詢可表達
- Room 可編譯
- 無明顯 query 命名混亂

---

## ISSUE-011 建立 Preferences DataStore 與 PreferenceKeys
### Goal
建立設定層資料來源。

### Depends on
- ISSUE-001

### Input docs
- Data Schema v1

### Scope
- user preferences file
- preference keys
- user preference data source

### Deliverables
- `PreferenceKeys.kt`
- `UserPreferences.kt`
- `UserPreferencesDataSource.kt`

### Acceptance Criteria
- 可讀寫基本設定
- keys 與 schema 文件一致

---

## ISSUE-012 建立 RSS Fetch 與 Feed Parser 骨架
### Goal
建立 Podcast RSS 取得與解析流程。

### Depends on
- ISSUE-001

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- HTTP feed fetch
- parser skeleton
- remote feed DTO / intermediate model

### Deliverables
- `PodcastFeedService.kt`
- `FeedParser.kt`
- 基本 feed model

### Acceptance Criteria
- 可輸入 RSS URL 並解析出節目與集數基本欄位

---

## ISSUE-013 建立 Podcast Local/Remote DataSource
### Goal
建立 podcast 資料來源抽象。

### Depends on
- ISSUE-010
- ISSUE-012
- ISSUE-011

### Input docs
- Project Structure Spec v1
- Data Schema v1

### Scope
- PodcastLocalDataSource
- PodcastRemoteDataSource
- EpisodeLocalDataSource

### Deliverables
- source classes
- 基本 mapper

### Acceptance Criteria
- local / remote 邊界清楚
- feature module 不直接碰 DAO / parser

---

## ISSUE-014 建立 Podcast / Episode / Settings Repository implementations
### Goal
建立 repository implementation 並串接 local/remote/preferences。

### Depends on
- ISSUE-013
- ISSUE-011

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- DefaultPodcastRepository
- DefaultEpisodeRepository
- DefaultSettingsRepository

### Deliverables
- repository implementation
- repository module binding

### Acceptance Criteria
- domain interface 可正常注入實作
- 功能邊界符合 TDS

---

## ISSUE-015 建立 RefreshPodcastFeedUseCase
### Goal
建立刷新 RSS feed 並更新 DB 的 use case。

### Depends on
- ISSUE-014

### Input docs
- Technical Design Spec v1

### Scope
- feed fetch
- parse
- compare
- insert/update episodes

### Deliverables
- `RefreshPodcastFeedUseCase.kt`

### Acceptance Criteria
- 輸入 feed URL 可寫入或更新 podcast/episode 資料
- 不產生重複集數

---

## ISSUE-016 建立 feature:podcast 訂閱列表與節目詳情頁
### Goal
讓使用者可看到已訂閱 podcast 與節目集數列表。

### Depends on
- ISSUE-015
- ISSUE-003

### Input docs
- PRD
- Project Structure Spec v1

### Scope
- PodcastListScreen
- PodcastDetailScreen
- EpisodeListScreen
- PodcastViewModel / PodcastDetailViewModel

### Deliverables
- 可顯示訂閱節目與集數的 UI

### Acceptance Criteria
- 畫面能讀到 DB 中資料
- 可進入節目詳情與集數列表

---

# 6. Phase 3：Radio 功能

## ISSUE-017 建立 Radio remote/local data source 與 repository
### Goal
建立電台資料來源與 repository。

### Depends on
- ISSUE-009
- ISSUE-010

### Input docs
- Data Schema v1
- Technical Design Spec v1

### Scope
- RadioRemoteDataSource
- RadioLocalDataSource
- DefaultRadioRepository

### Deliverables
- radio repository 實作

### Acceptance Criteria
- 可取得電台列表並快取至 DB

---

## ISSUE-018 建立 PlayRadioUseCase 與 radio media item builder
### Goal
提供 radio 播放的 domain 入口。

### Depends on
- ISSUE-017
- ISSUE-007

### Input docs
- Technical Design Spec v1

### Scope
- PlayRadioUseCase
- radio playable mapping
- prepare radio item

### Deliverables
- `PlayRadioUseCase.kt`
- radio playable mapper

### Acceptance Criteria
- 可從 station 資料建立可播放項並交給播放核心

---

## ISSUE-019 建立 feature:radio 電台列表頁
### Goal
建立電台列表 UI 與播放入口。

### Depends on
- ISSUE-018
- ISSUE-017
- ISSUE-003

### Input docs
- PRD
- Project Structure Spec v1

### Scope
- RadioScreen
- station list
- 點擊播放
- 基本收藏按鈕（可先做 placeholder）

### Deliverables
- RadioScreen
- RadioViewModel

### Acceptance Criteria
- 可列出 stations
- 點擊 station 可觸發播放

---

## ISSUE-020 建立 Radio 錯誤分類與重連 policy
### Goal
建立廣播專用的可重試/不可重試判斷與退避策略。

### Depends on
- ISSUE-004
- ISSUE-005

### Input docs
- Technical Design Spec v1

### Scope
- PlaybackErrorClassifier
- RadioReconnectPolicy
- backoff config

### Deliverables
- `PlaybackErrorClassifier.kt`
- `RadioReconnectPolicy.kt`

### Acceptance Criteria
- 可對暫時性網路錯誤與永久來源錯誤做區分
- 可輸出下一次 retry delay

---

## ISSUE-021 整合 Radio reconnect state machine 到 PlaybackService
### Goal
讓廣播播放在斷線時進入 reconnect 狀態並自動重試。

### Depends on
- ISSUE-020
- ISSUE-006
- ISSUE-018

### Input docs
- Technical Design Spec v1

### Scope
- service 內 reconnect flow
- Reconnecting state
- retry attempt 計數
- 최終 failed 狀態

### Deliverables
- PlaybackService radio reconnect integration

### Acceptance Criteria
- 模擬網路中斷時能進入 reconnect
- 網路恢復後可再試播放
- 達上限後顯示 failed

---

# 7. Phase 4：Podcast 播放與下載

## ISSUE-022 建立 PlayEpisodeUseCase 與 local/remote source resolution
### Goal
建立 podcast episode 播放入口，優先用本地檔，否則 fallback 到遠端 URL。

### Depends on
- ISSUE-014
- ISSUE-007
- ISSUE-009

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- episode playable mapping
- 本地檔存在檢查
- remote fallback

### Deliverables
- `PlayEpisodeUseCase.kt`

### Acceptance Criteria
- 已下載集數會走 local uri
- 未下載集數會走 remote url

---

## ISSUE-023 整合 Podcast 播放進度保存
### Goal
定期保存播放進度，暫停與完播時立即保存。

### Depends on
- ISSUE-022
- ISSUE-014

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- SavePlaybackProgressUseCase
- 15~30 秒節流寫入
- 暫停/完播立即寫入

### Deliverables
- `SavePlaybackProgressUseCase.kt`
- service progress save integration

### Acceptance Criteria
- 播放一段時間後重新進入可續播
- 完播後狀態正確更新

---

## ISSUE-024 建立 Episode 播放入口於 feature:podcast
### Goal
讓使用者可從集數列表直接播放 episode。

### Depends on
- ISSUE-022
- ISSUE-016

### Input docs
- PRD

### Scope
- episode row play action
- current playing 高亮或基本狀態

### Deliverables
- UI 互動整合

### Acceptance Criteria
- 點擊 episode 能開始播放
- PlayerScreen 能顯示對應內容

---

## ISSUE-025 建立 DownloadManager / DownloadService 骨架
### Goal
建立 podcast 下載核心基礎設施。

### Depends on
- ISSUE-005
- ISSUE-009
- ISSUE-010

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- Media3 DownloadManager wiring
- DownloadService
- app-level download state bridge

### Deliverables
- download service classes
- media module/update

### Acceptance Criteria
- 可接受下載請求並進入 queued/downloading 狀態

---

## ISSUE-026 建立 EnqueueEpisodeDownloadUseCase 與 DownloadRepository
### Goal
建立下載請求與狀態更新流程。

### Depends on
- ISSUE-025
- ISSUE-014

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- enqueue use case
- update DownloadRecordEntity
- update Episode.isDownloaded / isDownloading 快取欄位

### Deliverables
- `EnqueueEpisodeDownloadUseCase.kt`
- DefaultDownloadRepository

### Acceptance Criteria
- 可對指定 episode 建立下載任務
- DB 狀態同步正確

---

## ISSUE-027 建立 feature:downloads 下載列表頁
### Goal
建立下載列表與下載狀態顯示。

### Depends on
- ISSUE-026
- ISSUE-003

### Input docs
- PRD
- Project Structure Spec v1

### Scope
- DownloadsScreen
- 下載狀態展示
- 取消下載按鈕（若簡單可一併做）

### Deliverables
- DownloadsScreen
- DownloadsViewModel

### Acceptance Criteria
- 可看到 queued/downloading/completed 狀態

---

## ISSUE-028 建立 EvaluateAutoDownloadUseCase
### Goal
根據 app-level 與 subscription-level 規則判斷是否應自動下載新集數。

### Depends on
- ISSUE-015
- ISSUE-011
- ISSUE-014

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- global default + override 評估
- Wi-Fi only / metered 相關條件判斷

### Deliverables
- `EvaluateAutoDownloadUseCase.kt`

### Acceptance Criteria
- 對新集數輸入可輸出 shouldDownload 結果與原因

---

## ISSUE-029 建立 FeedRefreshWorker 並整合自動下載
### Goal
讓背景刷新 feed 後能自動把新集數加入下載佇列。

### Depends on
- ISSUE-028
- ISSUE-026
- ISSUE-015

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- FeedRefreshWorker
- WorkManager scheduling
- refresh -> evaluate -> enqueue flow

### Deliverables
- `FeedRefreshWorker.kt`
- worker scheduling entry

### Acceptance Criteria
- worker 執行後可刷新 feed
- 符合規則的新集數會進入下載佇列

---

# 8. Phase 5：中斷恢復與清理規則

## ISSUE-030 建立 ResumeAfterInterruptionPolicy
### Goal
建立來電或暫時性中斷後是否恢復播放的判斷規則。

### Depends on
- ISSUE-004
- ISSUE-011

### Input docs
- Technical Design Spec v1

### Scope
- interruption snapshot evaluation
- timeout / settings / manual pause 條件判斷

### Deliverables
- `ResumeAfterInterruptionPolicy.kt`

### Acceptance Criteria
- 能對 given snapshot + settings 輸出 shouldResume 結果

---

## ISSUE-031 整合 Audio Focus 與來電中斷流程
### Goal
讓播放服務能正確處理 focus loss / gain 與通話中斷。

### Depends on
- ISSUE-030
- ISSUE-006

### Input docs
- Technical Design Spec v1

### Scope
- request audio focus
- focus loss transient -> pause
- focus gain -> evaluate resume
- interruption snapshot 存取

### Deliverables
- PlaybackService interruption integration

### Acceptance Criteria
- 模擬 focus loss/gain 時行為符合規則
- 不會在不應恢復時亂播

---

## ISSUE-032 建立 becoming noisy / 耳機拔除處理
### Goal
支援耳機拔除自動暫停。

### Depends on
- ISSUE-031

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- receiver wiring
- noisy event handling

### Deliverables
- receiver + service integration

### Acceptance Criteria
- 耳機拔除時播放自動暫停

---

## ISSUE-033 建立 CleanupDownloadsUseCase
### Goal
建立下載清理規則與刪除候選判斷流程。

### Depends on
- ISSUE-026
- ISSUE-011
- ISSUE-010

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- cleanup candidate selection
- app-level rule + per-podcast override
- file delete + DB state update

### Deliverables
- `CleanupDownloadsUseCase.kt`

### Acceptance Criteria
- 可找出符合條件的舊下載
- 刪除後 DB 狀態一致

---

## ISSUE-034 建立 AutoCleanupWorker
### Goal
將清理規則背景化執行。

### Depends on
- ISSUE-033

### Input docs
- Technical Design Spec v1
- Project Structure Spec v1

### Scope
- worker class
- work request scheduling
- cleanup summary logging

### Deliverables
- `AutoCleanupWorker.kt`

### Acceptance Criteria
- worker 執行後能刪除符合條件的檔案
- 不會誤刪播放中或保留中的內容

---

# 9. Phase 6：設定、QA 與收尾

## ISSUE-035 建立 feature:settings 設定頁
### Goal
提供使用者調整關鍵播放與下載偏好。

### Depends on
- ISSUE-011
- ISSUE-003

### Input docs
- PRD
- Data Schema v1

### Scope
- Wi-Fi only
- auto delete mode
- resume after call
- default playback speed
- radio retry 開關與參數

### Deliverables
- SettingsScreen
- SettingsViewModel

### Acceptance Criteria
- 調整設定後可寫入 DataStore
- 設定值可回填 UI

---

## ISSUE-036 建立手動 QA 測試清單與測試輔助紀錄
### Goal
建立手動驗收基礎，方便人工與其他 AI 驗證。

### Depends on
- ISSUE-021
- ISSUE-029
- ISSUE-034
- ISSUE-031

### Input docs
- PRD
- Technical Design Spec v1

### Scope
- QA checklist
- 測試場景紀錄格式
- bug 回報模板

### Deliverables
- `QA_CHECKLIST.md`
- bug template

### Acceptance Criteria
- 至少涵蓋：斷網/恢復、來電/掛斷、耳機拔除、離線播放、下載清理

---

## ISSUE-037 建立單元測試：Policy 與 Evaluator
### Goal
為核心規則建立 unit tests。

### Depends on
- ISSUE-020
- ISSUE-028
- ISSUE-030
- ISSUE-033

### Input docs
- Technical Design Spec v1

### Scope
- RadioReconnectPolicy tests
- EvaluateAutoDownloadUseCase tests
- ResumeAfterInterruptionPolicy tests
- CleanupDownloadsUseCase candidate tests

### Deliverables
- unit test files

### Acceptance Criteria
- 核心規則測試皆通過

---

## ISSUE-038 建立整合測試：Feed refresh / download / progress
### Goal
驗證 Podcast 關鍵資料流可正常串接。

### Depends on
- ISSUE-029
- ISSUE-023

### Input docs
- Technical Design Spec v1
- Data Schema v1

### Scope
- feed refresh -> episodes inserted
- auto download enqueue
- playback progress save / reload

### Deliverables
- integration test files

### Acceptance Criteria
- 關鍵資料流測試可跑通

---

## ISSUE-039 Bug bash 與穩定性收尾
### Goal
整合修補最終 MVP 必要 bug。

### Depends on
- ISSUE-036
- ISSUE-037
- ISSUE-038

### Input docs
- 全部核心文件

### Scope
- 修正播放狀態錯亂
- 修正下載與清理邏輯問題
- 修正 UI 基本穩定性問題

### Deliverables
- bug fix commits
- release notes draft

### Acceptance Criteria
- MVP 主流程可穩定通過驗收

---

# 10. 建議開發順序（最精簡可跑版本）

若你要最短路徑先做出 MVP，建議順序如下：

1. ISSUE-001
2. ISSUE-002
3. ISSUE-003
4. ISSUE-004
5. ISSUE-005
6. ISSUE-006
7. ISSUE-007
8. ISSUE-009
9. ISSUE-010
10. ISSUE-011
11. ISSUE-012
12. ISSUE-013
13. ISSUE-014
14. ISSUE-015
15. ISSUE-016
16. ISSUE-017
17. ISSUE-018
18. ISSUE-019
19. ISSUE-020
20. ISSUE-021
21. ISSUE-022
22. ISSUE-023
23. ISSUE-024
24. ISSUE-025
25. ISSUE-026
26. ISSUE-027
27. ISSUE-028
28. ISSUE-029
29. ISSUE-030
30. ISSUE-031
31. ISSUE-032
32. ISSUE-033
33. ISSUE-034
34. ISSUE-035
35. ISSUE-036
36. ISSUE-037
37. ISSUE-038
38. ISSUE-039

---

# 11. 建議可平行處理的任務群

## 群組 A：基礎骨架
- ISSUE-001
- ISSUE-002
- ISSUE-003

## 群組 B：資料層
- ISSUE-009
- ISSUE-010
- ISSUE-011
- ISSUE-012

## 群組 C：播放核心
- ISSUE-004
- ISSUE-005
- ISSUE-006
- ISSUE-007
- ISSUE-008

## 群組 D：Podcast 流程
- ISSUE-013
- ISSUE-014
- ISSUE-015
- ISSUE-016
- ISSUE-022
- ISSUE-023
- ISSUE-024

## 群組 E：Radio 流程
- ISSUE-017
- ISSUE-018
- ISSUE-019
- ISSUE-020
- ISSUE-021

## 群組 F：下載與清理
- ISSUE-025
- ISSUE-026
- ISSUE-027
- ISSUE-028
- ISSUE-029
- ISSUE-033
- ISSUE-034

## 群組 G：中斷與設定
- ISSUE-030
- ISSUE-031
- ISSUE-032
- ISSUE-035

---

# 12. 直接給 AI Agent 的單張任務模板

```text
你現在要執行 ISSUE-XXX。

請先閱讀以下文件：
1. PRD + 技術架構清單 + MVP 功能表
2. Technical Design Spec v1
3. Data Schema v1
4. Project Structure Spec v1
5. Issue List v1（只看 ISSUE-XXX 與其依賴 issue）

你的任務：
- 僅完成 ISSUE-XXX scope 內的內容
- 不可擴做其他 issue
- 不可自行改變 package/module 擺放規則
- 不可自行修改資料 schema 或狀態命名

輸出格式：
1. 你理解的目標
2. 你要新增/修改的檔案清單
3. 實作內容
4. 驗收對照
5. 風險與待確認點
```

---

# 13. 給你自己的使用建議

## 13.1 第一次丟給其他 AI 時
不要直接丟 ISSUE-001 到 ISSUE-039 全部。

應該先丟：
- ISSUE-001
- ISSUE-002
- ISSUE-003

確認它有遵守 module 結構後，再丟下一批。

## 13.2 第二批最適合
- ISSUE-004
- ISSUE-005
- ISSUE-006
- ISSUE-007

因為這能先驗證它會不會把 ExoPlayer 亂塞到 feature。

## 13.3 第三批再丟資料流
- ISSUE-009 ~ ISSUE-015

---

# 14. v1 結論
這份 Issue List v1 已將本專案拆成可落地執行的任務，具備以下特性：
- 可逐張發給 AI Agent
- 可直接轉成 GitHub Issues
- 有明確依賴順序
- 可平行分工
- 能避免 AI 一次做太大範圍而失控

此文件完成後，你已經擁有一套完整的 AI 可執行開發文件組：
1. PRD
2. TDS
3. Data Schema
4. Project Structure
5. Issue List

下一份最有價值的文件會是：
**AI Agent Execution Pack**

因為那份文件可以直接規定「其他 AI 如何讀文件、如何回報、如何分批執行、如何處理 blocker」，讓整體開發流程更穩。

