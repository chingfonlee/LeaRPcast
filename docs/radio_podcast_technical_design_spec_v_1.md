# Technical Design Spec v1

## 專案名稱
Android 廣播與 Podcast 收聽 App

## 文件版本
v1.0

## 文件目的
本文件定義 Android 廣播與 Podcast 收聽 App 的技術實作藍圖，作為後續資料表設計、專案資料夾結構、Issue 拆解與 AI Agent 實作的依據。

本文件回答以下問題：
- 系統要如何切模組
- 播放核心如何運作
- 廣播斷線重連如何設計
- Podcast 下載與清理如何運作
- 來電中斷與恢復如何判定
- Room、DataStore、WorkManager、Media3 各自負責什麼

---

# 1. 設計原則

## 1.1 核心原則
1. **Playback-first**：任何 UI、資料同步、背景下載，都不能破壞播放穩定性。
2. **Service-centered**：播放狀態以 `MediaSessionService` 為中心，UI 只是控制端與顯示端。
3. **Local-first**：訂閱、播放進度、下載狀態、設定皆應能在本機獨立運作。
4. **Resumable UX**：廣播斷線後盡量恢復；Podcast 優先支援離線穩定播放。
5. **Explicit state**：播放、下載、重連、中斷都必須是明確可觀測狀態，不靠隱性旗標。
6. **Separation of concerns**：播放、下載、feed 更新、設定管理彼此解耦。

## 1.2 非目標
以下內容不納入 v1：
- 雲端帳號同步
- 多裝置進度同步
- 推薦系統
- 社群分享
- 跨平台共用核心
- Android Auto 深度整合

---

# 2. 系統總體架構

## 2.1 高層架構
系統分為五個核心子系統：

1. **Playback System**
   - 負責廣播與 podcast 播放
   - 管理前景服務、媒體通知、audio focus、耳機與藍牙控制

2. **Catalog System**
   - 管理電台列表、Podcast 訂閱、Feed 解析、集數資料

3. **Download System**
   - 管理 podcast 自動下載、手動下載、清理規則、儲存空間策略

4. **Interruption & Recovery System**
   - 管理來電、耳機拔除、網路中斷、網路恢復、audio focus 喪失與恢復

5. **Settings & Persistence System**
   - 管理使用者偏好、播放進度、下載紀錄、清理規則

## 2.2 運行時架構

### 常駐元件
- `PlaybackService` (`MediaSessionService`)
- `MediaSession`
- 單一 `ExoPlayer` instance

### 背景元件
- `FeedRefreshWorker`
- `AutoCleanupWorker`
- 視需求：`AutoDownloadCoordinator`（由 Worker 驅動）

### 前台 UI 元件
- Radio Screen
- Podcast Screen
- Episode List Screen
- Player Screen
- Downloads Screen
- Settings Screen

---

# 3. 模組切分

## 3.1 推薦模組結構

```text
app
core:common
core:ui
core:network
core:database
core:datastore
core:media
core:testing
feature:radio
feature:podcast
feature:player
feature:downloads
feature:settings
domain
```

## 3.2 模組責任

### `app`
- Application
- Hilt 初始化
- Navigation host
- 全域設定綁定

### `core:common`
- 共用 Result / Error / Dispatcher / Logger / TimeProvider
- 共用 enum、sealed class、utility

### `core:ui`
- 共用 Compose 元件
- 共用 UI state model
- 主題、字體、Snackbar 規則

### `core:network`
- Retrofit / OkHttp
- RSS / HTTP request
- 網路錯誤轉換
- 電台串流來源 API

### `core:database`
- Room database
- DAO
- Entity
- Migration

### `core:datastore`
- 使用者偏好設定
- 播放偏好
- 自動下載與清理規則

### `core:media`
- ExoPlayer factory
- MediaItem builder
- Audio attributes
- Load error policy
- Playback event mapper
- MediaSession callback helpers

### `domain`
- UseCase
- Repository interface
- 核心業務規則

### `feature:radio`
- 電台列表與播放入口
- Radio-specific UI / ViewModel / UseCase glue

### `feature:podcast`
- 訂閱、feed、集數列表、節目詳情

### `feature:player`
- 播放控制 UI
- Player 狀態觀察
- 當前播放項目顯示

### `feature:downloads`
- 下載列表
- 手動下載 / 取消下載
- 自動清理狀態顯示

### `feature:settings`
- 偏好設定畫面
- Wi-Fi only、自動刪除、通話後恢復等設定

---

# 4. 播放核心設計

## 4.1 單一播放器策略
系統採用單一 `ExoPlayer` instance。

原因：
- 廣播與 podcast 不需要同時播放
- 單一播放器可集中管理 audio focus、notification、media session
- 可以簡化 UI 與 Service 狀態同步
- 可避免多播放器搶資源與行為衝突

## 4.2 播放服務設計

### 類別
`PlaybackService : MediaSessionService`

### 職責
- 建立與持有 `ExoPlayer`
- 建立 `MediaSession`
- 對外提供媒體控制
- 將播放狀態轉換為 UI 可觀測狀態
- 管理 foreground notification
- 接收 audio focus / noisy intent / interruption
- 管理廣播重連狀態

## 4.3 播放類型
播放分兩大類：

### A. Radio Stream
- 類型：Live audio stream
- 來源：HLS 優先，次之 progressive audio
- 特性：不可依賴完整下載，重點在 buffer 與 reconnect

### B. Podcast Episode
- 類型：On-demand audio
- 來源：RSS feed episode URL
- 特性：可串流，也可離線播放已下載檔案

## 4.4 MediaItem 抽象
建立統一的 `PlayableItem` 抽象：

```kotlin
sealed interface PlayableItem {
    val id: String
    val title: String
    val subtitle: String?
    val artworkUrl: String?
}

data class RadioPlayable(...): PlayableItem

data class EpisodePlayable(...): PlayableItem
```

用途：
- UI 不需關心底層來源差異
- Player Screen 可共用
- Analytics / logging 可統一處理

## 4.5 Player State Model
建立可觀測的顯式狀態：

```kotlin
sealed interface PlaybackStateModel {
    data object Idle : PlaybackStateModel
    data class Loading(val item: PlayableItem) : PlaybackStateModel
    data class Playing(val item: PlayableItem, val positionMs: Long, val isLive: Boolean) : PlaybackStateModel
    data class Buffering(val item: PlayableItem) : PlaybackStateModel
    data class Reconnecting(val item: PlayableItem, val attempt: Int) : PlaybackStateModel
    data class Paused(val item: PlayableItem, val positionMs: Long) : PlaybackStateModel
    data class Completed(val item: PlayableItem) : PlaybackStateModel
    data class Failed(val item: PlayableItem?, val reason: PlaybackError) : PlaybackStateModel
}
```

設計原則：
- Radio 與 Podcast 都走同一狀態模型
- `Reconnecting` 僅供 Radio 使用
- `Completed` 通常僅供 Podcast 使用

---

# 5. 廣播重連設計

## 5.1 設計目標
- 短暫網路波動時，不要求使用者手動重播
- 網路恢復時，盡快自動再試一次
- 若來源本身失效，不能無限重試

## 5.2 錯誤分類
播放錯誤分為兩類：

### 可重試錯誤
- 暫時性網路錯誤
- Socket timeout
- Connection reset
- DNS/temporary connectivity issue
- Network lost while streaming

### 不可重試錯誤
- 404 / 410
- 明確來源不存在
- 不支援格式
- 權限錯誤 / 403
- 連續多次失敗超過上限

## 5.3 重連狀態機

```text
Idle
 -> Loading
 -> Playing
 -> Buffering
 -> Reconnecting(attempt=1)
 -> Reconnecting(attempt=2)
 -> Reconnecting(attempt=3)
 -> Playing
 or -> Failed
```

## 5.4 重試策略
建議採指數退避：
- 第 1 次：1 秒
- 第 2 次：2 秒
- 第 3 次：4 秒
- 第 4 次：8 秒
- 第 5 次：15 秒

上限建議：
- 單輪最多 5 次
- 若 `ConnectivityManager` 回報網路恢復，可重置 backoff 並立即嘗試一次

## 5.5 Network Recovery Hook
使用 `ConnectivityManager.NetworkCallback`：
- `onLost()`：若目前為 radio，進入 reconnect flow
- `onAvailable()`：若目前為 reconnecting，立即觸發一次 retry

## 5.6 UI 顯示規則
Radio 在 reconnecting 時：
- 顯示「重新連線中」
- 顯示目前重試次數
- 保留停止與手動重試按鈕
- 不要直接清空 currently playing UI

---

# 6. Podcast 播放與下載設計

## 6.1 播放模式
Podcast 支援兩種來源：

### A. Streaming Mode
- 集數未下載
- 直接以遠端 URL 串流播放

### B. Offline Mode
- 若本地已存在有效下載檔案
- 優先使用本地 URI 播放

切換規則：
- `EpisodePlayable` 建立前先查詢本地下載狀態
- 若下載完成且檔案存在，使用 local URI
- 否則 fallback 到 remote URL

## 6.2 Feed 更新流程

```text
WorkManager trigger
 -> fetch RSS
 -> parse feed
 -> compare with local DB
 -> insert new episodes
 -> update existing metadata if needed
 -> enqueue auto-download if rules matched
```

## 6.3 下載流程
下載流程使用 Media3 DownloadManager/DownloadService 管理。

### 設計原則
- 下載應與播放解耦
- 下載狀態需持久化
- 下載任務在 App 關閉後仍可恢復

### 流程
```text
User/Worker requests download
 -> build DownloadRequest
 -> enqueue to DownloadManager
 -> DownloadService 執行
 -> persist status
 -> UI observe status changes
```

## 6.4 自動下載規則
預設規則：
- 僅 Wi-Fi
- 僅下載最新集數
- 每個節目保留最近 5 集下載

每個節目可覆寫：
- 是否自動下載
- 保留集數數量
- 是否允許行動網路

## 6.5 自動刪除規則
刪除候選條件：
- 已播放完成
- 已超過刪除門檻（例如 24 小時）
- 非收藏
- 非播放中
- 非手動保留
- 非下載中

刪除優先順序：
1. 已播放且過期
2. 非活躍舊集數
3. 超過保留數量的舊下載

## 6.6 清理協調器
建立 `CleanupDownloadsUseCase`：
- 可由 `AutoCleanupWorker` 觸發
- 也可在下載前做預檢時觸發
- 輸出清理結果摘要

---

# 7. 音訊中斷與恢復設計

## 7.1 中斷來源
- 電話來電 / 通話
- 其他 App 搶佔 audio focus
- 耳機拔除（becoming noisy）
- 藍牙裝置斷線

## 7.2 Interruption Snapshot
在中斷發生前，記錄快照：

```kotlin
data class InterruptionSnapshot(
    val itemId: String,
    val itemType: ItemType,
    val wasPlaying: Boolean,
    val positionMs: Long,
    val interruptedAtEpochMs: Long,
    val cause: InterruptionCause,
)
```

## 7.3 暫停規則
若發生：
- `AUDIOFOCUS_LOSS_TRANSIENT`
- 電話來電
- noisy event

則：
- 若當前正在播放，執行 pause
- 建立 `InterruptionSnapshot`

## 7.4 恢復規則
僅在以下條件成立時自動恢復：
1. 中斷前是播放中
2. 使用者沒有在中斷期間手動停止或切換內容
3. 中斷原因允許恢復
4. 使用者設定允許自動恢復
5. 中斷持續時間未超過門檻

建議門檻：60 分鐘

## 7.5 不自動恢復的情境
- 使用者手動 pause
- 使用者在中斷後主動播放別的內容
- 中斷時間過長
- 耳機被拔除後改由外放可能造成尷尬時，預設不自動 resume

## 7.6 Audio Focus 處理策略
- request focus before playback start/resume
- focus lost transient -> pause
- focus gained -> evaluate resume policy
- focus lost permanent -> stop or remain paused by design

---

# 8. 資料持久化邊界

## 8.1 Room 責任
Room 儲存「有結構、可查詢、需要關聯」的資料：
- Podcast 節目
- Episode 集數
- Subscription 訂閱
- PlaybackProgress
- DownloadRecord
- QueueItem
- RadioStation（若需本地快取）

## 8.2 DataStore 責任
DataStore 儲存「偏好設定與小型狀態」：
- Wi-Fi only
- 通話後自動恢復開關
- 自動刪除規則
- 預設播放速度
- 每節目自動下載預設值
- 睡眠計時器偏好

## 8.3 不持久化內容
下列資料不直接永久存 DB：
- ExoPlayer 原始 runtime state
- 當前 transient buffering 細節
- 單次 reconnect attempt 計數
- 短生命週期 UI-only snackbar state

---

# 9. Repository 與 UseCase 邊界

## 9.1 Repository Interface

### RadioRepository
- 取得電台列表
- 取得電台詳情
- 快取來源

### PodcastRepository
- 取得訂閱列表
- 新增/取消訂閱
- 刷新 feed

### EpisodeRepository
- 取得節目集數列表
- 更新集數 metadata
- 取得可播放集數資料

### PlaybackRepository
- 儲存/讀取播放進度
- 寫入最近播放紀錄

### DownloadRepository
- 加入下載
- 取消下載
- 觀察下載狀態
- 刪除本地下載檔

### SettingsRepository
- 讀寫使用者偏好

## 9.2 UseCase 建議
- `PlayRadioUseCase`
- `PlayEpisodeUseCase`
- `PausePlaybackUseCase`
- `StopPlaybackUseCase`
- `ResumeAfterInterruptionUseCase`
- `RefreshPodcastFeedUseCase`
- `SubscribePodcastUseCase`
- `EnqueueEpisodeDownloadUseCase`
- `EvaluateAutoDownloadUseCase`
- `CleanupDownloadsUseCase`
- `SavePlaybackProgressUseCase`

---

# 10. 主要流程設計

## 10.1 Radio 播放流程

```text
User taps station
 -> PlayRadioUseCase
 -> PlaybackService.prepareRadio(mediaItem)
 -> request audio focus
 -> exoPlayer.prepare()
 -> state Loading
 -> state Playing
 -> if network lost -> Buffering/Reconnecting
 -> retry/backoff
 -> Playing or Failed
```

## 10.2 Podcast 串流播放流程

```text
User taps episode
 -> resolve local file or remote url
 -> PlayEpisodeUseCase
 -> PlaybackService.prepareEpisode(mediaItem)
 -> request audio focus
 -> exoPlayer.prepare()
 -> Loading -> Playing
 -> position periodically saved
 -> Completed or Paused
```

## 10.3 自動下載流程

```text
FeedRefreshWorker
 -> RefreshPodcastFeedUseCase
 -> detect new episodes
 -> EvaluateAutoDownloadUseCase
 -> EnqueueEpisodeDownloadUseCase
 -> DownloadManager executes
 -> persist download state
```

## 10.4 清理流程

```text
AutoCleanupWorker
 -> CleanupDownloadsUseCase
 -> collect eligible items
 -> delete local files
 -> update DB records
 -> emit summary
```

## 10.5 來電中斷恢復流程

```text
Playback active
 -> interruption occurs
 -> save InterruptionSnapshot
 -> pause
 -> interruption ends
 -> evaluate resume conditions
 -> request focus
 -> resume or stay paused
```

---

# 11. 狀態同步設計

## 11.1 單一真實來源
播放器相關狀態以 `PlaybackService` 為單一真實來源。

UI 不直接管理播放事實，只觀察 `PlaybackService` 對外暴露的狀態流。

## 11.2 狀態傳播方式
建議：
- Service 內部以 `StateFlow<PlaybackStateModel>` 維護
- UI ViewModel 透過 controller / binder / repository bridge 收集狀態
- ViewModel 再映射成 screen-specific UI state

## 11.3 進度寫回策略
為避免頻繁 I/O：
- 播放進度每 15~30 秒寫入一次
- 暫停時立即寫入
- App 進背景時可額外寫入一次
- 完播時寫入 completed 狀態

---

# 12. 錯誤處理設計

## 12.1 PlaybackError 模型

```kotlin
sealed interface PlaybackError {
    data object NetworkTemporary : PlaybackError
    data object SourceUnavailable : PlaybackError
    data object UnsupportedFormat : PlaybackError
    data object AudioFocusDenied : PlaybackError
    data object FileMissing : PlaybackError
    data class Unknown(val message: String?) : PlaybackError
}
```

## 12.2 UI 錯誤處理原則
- 能自動恢復的錯誤，不先打斷畫面
- 只有在最終失敗時才顯示強提示
- 訊息需分清：
  - 網路暫時中斷
  - 來源失效
  - 本地檔案遺失
  - 無法取得音訊焦點

---

# 13. 觀測與日誌

## 13.1 需要記錄的事件
- play requested
- playback started
- buffering started
- reconnect attempt
- reconnect success
- reconnect failed
- interruption occurred
- auto resume success/blocked
- download enqueued
- download completed
- auto cleanup executed

## 13.2 日誌用途
- 偵錯播放穩定性
- 分析廣播重連成功率
- 分析通話後恢復是否造成 UX 問題
- 分析下載與清理規則是否過 aggressive

---

# 14. 測試策略概述

## 14.1 Unit Test
- resume policy evaluator
- cleanup eligibility evaluator
- auto-download evaluator
- playback state mapper

## 14.2 Integration Test
- feed refresh -> new episodes -> download enqueue
- playback progress save/reload
- download complete -> local play resolution

## 14.3 Manual QA Priority
- Wi-Fi 斷線/恢復
- 行動網路切換
- 來電/掛電話
- 耳機拔除
- 背景播放 30 分鐘以上
- 已下載 podcast 飛航模式播放

---

# 15. 待後續子文件承接的內容
本文件之後，應拆出三份子文件：

1. **資料表設計文件**
   - Room entity
   - DAO
   - index
   - migration 初稿
   - DataStore key list

2. **專案資料夾結構文件**
   - package 命名
   - feature/layer 實際擺放規則
   - UI / domain / data 層檔案範本

3. **開發任務清單（Issue List）**
   - 依相依順序拆成可執行 task
   - 每個 task 定義輸入/輸出/驗收條件

---

# 16. v1 結論
本設計採用：
- **單一播放器 + MediaSessionService** 作為播放核心
- **Radio 重連狀態機** 作為穩定收聽關鍵
- **Podcast 離線優先 + DownloadManager + WorkManager** 作為下載策略
- **Interruption Snapshot + Resume Policy** 作為來電中斷恢復基礎
- **Room 存結構資料 / DataStore 存偏好設定** 作為持久化分工

這份 TDS v1 的角色是「技術母文件」。
後續所有資料表、資料夾結構、Issue 拆解，都應以這份文件為準，不應自行偏離。

