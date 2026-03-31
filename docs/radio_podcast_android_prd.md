# Android 廣播與 Podcast 收聽 App PRD

## 1. 專案概述

### 1.1 專案名稱
暫名：RadioCast

### 1.2 專案目標
打造一款以「穩定收聽體驗」為核心的 Android 音訊 App，支援：
- 廣播即時串流收聽
- Podcast 訂閱、播放、離線下載
- 網路不穩時的可恢復播放體驗
- 電話/其他音訊事件中斷時的合理暫停與恢復
- 自動化下載與儲存空間管理

### 1.3 核心價值
1. **不中斷感**：廣播遇到短暫斷網時盡量自動恢復。
2. **離線可用**：Podcast 可預先下載，弱網或無網也能聽。
3. **低管理成本**：自動下載、自動清理，不讓使用者手動管理太多。
4. **符合直覺**：電話進來自動暫停，掛掉後在合理條件下繼續播放。
5. **背景穩定**：鎖屏、切到背景、切換 App 時仍能穩定播放。

---

## 2. 產品定位

### 2.1 目標使用者
- 通勤族：希望廣播不中斷、開車或騎車時免手動操作
- Podcast 重度使用者：需要自動下載、清理、播放進度同步
- 弱網環境使用者：常在電梯、地下室、移動中收聽
- 重視簡潔體驗者：希望設定合理、預設可直接使用

### 2.2 產品定位句
一款重視「穩定播放、離線收聽、低干擾管理」的 Android 廣播與 Podcast App。

---

## 3. 問題定義

### 3.1 使用者痛點
- 廣播串流在網路波動時容易停掉，且不會自動恢復
- Podcast 沒有先下載時，在通勤途中容易卡頓
- 下載太多集數後容易佔滿儲存空間
- 來電或其他音訊事件中斷後，播放器可能不恢復，或恢復得太突兀
- 背景播放時被系統回收、通知控制不穩、藍牙/耳機切換表現差

### 3.2 產品要解決的核心問題
1. 如何讓廣播在網路不穩時維持「可恢復」而不是直接失敗
2. 如何讓 Podcast 下載策略既省流量又實用
3. 如何讓播放中斷與恢復符合大多數使用者預期
4. 如何降低使用者對儲存空間管理的負擔

---

## 4. 成功指標（MVP 階段）

### 4.1 產品 KPI
- 廣播播放成功啟動率 >= 98%
- Podcast 已下載集數離線播放成功率 >= 99%
- 通話結束後恢復播放成功率 >= 95%（僅限符合恢復條件的情境）
- 自動下載任務成功率 >= 95%
- 因儲存空間不足導致下載失敗比例 < 5%

### 4.2 UX 指標
- 使用者第一次安裝後 3 分鐘內可完成：
  - 播放一個廣播
  - 訂閱一個 podcast
  - 播放一個 podcast 集數
- 主要播放頁面 3 次點擊內可完成下載/加入播放佇列/調整速度

---

## 5. 功能範圍

### 5.1 廣播功能
#### MVP 必要功能
- 電台列表
- 點擊即播
- 背景播放
- 鎖屏/通知控制
- 音訊焦點管理
- 網路中斷後自動重試
- 顯示「重新連線中」狀態
- 手動重試播放

#### 後續版本
- 收藏電台
- 最近收聽
- 類型/地區篩選
- 睡眠計時器
- 廣播節目表

### 5.2 Podcast 功能
#### MVP 必要功能
- RSS 訂閱
- Feed 更新
- 集數列表
- 串流播放
- 離線下載
- 播放速度調整
- 播放進度記錄
- 自動下載最新集數
- 自動刪除已播放集數
- 儲存空間限制與清理策略

#### 後續版本
- 章節資訊
- 跳過片頭/片尾
- 智慧下載
- 收藏/書籤
- OPML 匯入匯出
- 搜尋與推薦

### 5.3 音訊控制功能
#### MVP 必要功能
- 接聽電話自動暫停
- 通話結束後在符合條件下恢復播放
- 耳機拔除自動暫停
- 藍牙媒體按鈕支援
- 車載/外部裝置控制相容

### 5.4 儲存管理功能
#### MVP 必要功能
- 只在 Wi‑Fi 自動下載（預設）
- 可切換允許行動網路下載
- 已播放 24 小時後自動刪除下載檔（預設）
- 每個 podcast 預設最多保留最近 5 集下載內容
- 收藏與播放中集數不自動刪除

---

## 6. 預設產品策略

### 6.1 廣播播放策略
- 優先支援 HLS
- 若來源不支援 HLS，支援 progressive audio stream
- 網路斷線時進入 Reconnecting 狀態
- 採用指數退避重試（例如 1s / 2s / 4s / 8s）
- 偵測到網路恢復時立即再試一次
- 若連續失敗超過門檻，顯示播放失敗並提供一鍵重試

### 6.2 Podcast 自動下載策略
- 預設只在 Wi‑Fi 下自動下載
- 訂閱後可啟用自動下載最新集數
- 背景刷新 feed 時，如有新集數且符合條件，自動加入下載佇列
- 若裝置空間不足，先清理符合規則的舊下載，再重試下載

### 6.3 Podcast 自動刪除策略
- 已播放完成的下載內容，預設於 24 小時後刪除
- 每個節目可選擇：
  - 播放完立刻刪除
  - 24 小時後刪除
  - 永不自動刪除
- 收藏、加入佇列中、播放中、手動標記保留的集數，不自動刪除

### 6.4 通話中斷與恢復策略
- 來電或通話中斷時自動暫停
- 僅在下列條件成立時自動恢復：
  1. 中斷前確實正在播放
  2. 使用者不是手動暫停
  3. 中斷後未超過可恢復時間門檻（例如 60 分鐘）
  4. 使用者未關閉「通話後自動恢復」設定

---

## 7. 使用者流程

### 7.1 廣播收聽流程
1. 開啟 App
2. 進入 Radio 頁面
3. 選擇電台
4. 開始播放
5. 若網路斷線，播放器顯示「重新連線中」
6. 網路恢復後自動續播，或由使用者點擊重試

### 7.2 Podcast 訂閱與自動下載流程
1. 使用者新增 RSS 或從目錄中訂閱
2. App 解析 feed 並建立節目資料
3. 若開啟自動下載，新集數將在符合網路條件時下載
4. 使用者可於離線狀態播放已下載內容
5. 已播放集數依規則自動刪除

### 7.3 通話中斷流程
1. 使用者播放中
2. 來電觸發 audio focus loss / interruption
3. 播放器暫停並記錄 interruption 前狀態
4. 通話結束後請求 focus
5. 若符合恢復條件，自動續播

---

## 8. 非功能需求

### 8.1 穩定性
- 播放服務需能在背景持續運作
- Service 與 UI 解耦，Activity 被銷毀時不影響播放
- 播放器異常需有狀態回報與可恢復機制

### 8.2 效能
- 首次播放啟動時間應盡量壓低
- 背景下載不得造成 UI 明顯卡頓
- Feed 更新與下載排程需可批次化

### 8.3 可維護性
- 採 Clean Architecture
- Domain/Player/Download/Storage 模組邊界清楚
- 所有播放器事件、下載事件、UI state 可追蹤

### 8.4 可測試性
- 播放器狀態機可單元測試
- 下載流程可整合測試
- 中斷恢復規則可透過 fake player / fake audio focus 模擬測試

---

## 9. 技術架構清單

### 9.1 技術選型
- **語言**：Kotlin
- **UI**：Jetpack Compose
- **播放核心**：AndroidX Media3 / ExoPlayer
- **背景播放**：MediaSessionService
- **媒體控制**：MediaSession + MediaController
- **下載管理**：Media3 DownloadManager + DownloadService
- **背景任務**：WorkManager
- **本地資料庫**：Room
- **偏好設定**：DataStore
- **網路層**：Retrofit + OkHttp
- **RSS 解析**：自建 parser 或 Rome/XmlPullParser 類型方案
- **DI**：Hilt
- **併發**：Coroutines + Flow
- **日誌/觀測**：Timber + Crashlytics（選配）

### 9.2 模組切分
- `app`
- `core:ui`
- `core:network`
- `core:database`
- `core:datastore`
- `feature:radio`
- `feature:podcast`
- `feature:player`
- `feature:downloads`
- `feature:settings`
- `domain`

### 9.3 資料分層
#### Data Layer
- RadioStationRepository
- PodcastRepository
- EpisodeRepository
- PlaybackRepository
- DownloadRepository
- SettingsRepository

#### Domain Layer
- PlayRadioUseCase
- PlayEpisodeUseCase
- PausePlaybackUseCase
- ResumeAfterInterruptionUseCase
- RefreshFeedsUseCase
- EnqueueAutoDownloadUseCase
- CleanupDownloadsUseCase

#### Presentation Layer
- RadioViewModel
- PodcastViewModel
- PlayerViewModel
- DownloadsViewModel
- SettingsViewModel

### 9.4 播放架構
- `PlaybackService : MediaSessionService`
- 內含單一 `ExoPlayer`
- 由 `MediaSession` 對外暴露控制能力
- UI 透過 `MediaController` 綁定 Service
- 播放狀態以 Flow/StateFlow 派發至 UI

### 9.5 廣播穩定性架構
- 自訂 `LoadErrorHandlingPolicy`
- 網路連線監聽 `ConnectivityManager.NetworkCallback`
- 播放錯誤分類：
  - 暫時性網路錯誤 → 自動重試
  - 來源失效 / 404 / 權限錯誤 → 直接失敗
- 播放狀態機：
  - Idle
  - Loading
  - Playing
  - Buffering
  - Reconnecting
  - Paused
  - Failed

### 9.6 Podcast 下載架構
- Feed 更新：WorkManager 定期執行
- 新集數檢查：解析 RSS 並比對 DB
- 自動下載：加入 DownloadManager 佇列
- 下載狀態持久化：DownloadIndex / Room 額外索引
- 清理任務：WorkManager 觸發 CleanupDownloadsUseCase

### 9.7 資料儲存設計
#### Room
- Podcast
- Episode
- Subscription
- PlaybackProgress
- DownloadRecord
- QueueItem

#### DataStore
- 自動下載開關
- Wi‑Fi only
- 自動刪除規則
- 通話後自動恢復
- 播放速度預設值
- 睡眠計時器偏好

---

## 10. MVP 功能表

### 10.1 MVP 範圍（必做）
#### A. 播放
- [ ] 廣播串流播放
- [ ] Podcast 串流播放
- [ ] Podcast 離線播放
- [ ] 後台播放
- [ ] 鎖屏通知控制
- [ ] MediaSession 控制

#### B. 穩定性
- [ ] 網路中斷後廣播自動重試
- [ ] 網路恢復後自動再嘗試播放
- [ ] 通話來時自動暫停
- [ ] 通話結束後條件式恢復播放
- [ ] 耳機拔除自動暫停

#### C. Podcast 管理
- [ ] RSS 訂閱
- [ ] 集數清單
- [ ] 自動下載最新集數
- [ ] 已播放內容自動刪除
- [ ] 每節目下載保留上限

#### D. 設定
- [ ] Wi‑Fi only 自動下載
- [ ] 是否允許行動網路下載
- [ ] 通話後自動恢復開關
- [ ] 自動刪除規則設定
- [ ] 播放速度設定

### 10.2 非 MVP（延後）
- [ ] 搜尋推薦
- [ ] OPML 匯入匯出
- [ ] 雲端同步
- [ ] 章節資訊
- [ ] 跳過片頭/片尾
- [ ] Android Auto 深度整合
- [ ] Wear OS 控制

---

## 11. 主要風險與對策

### 11.1 廣播來源不穩定
**風險**：來源本身失效、跨區限制、串流格式不一致
**對策**：
- 來源健康檢查
- 支援多種來源格式
- 錯誤分類與 UI 明確提示

### 11.2 背景播放被系統限制
**風險**：新版本 Android 對背景與前景服務限制更嚴格
**對策**：
- 嚴格採 MediaSessionService + foreground service 模式
- 正確處理通知與生命週期

### 11.3 儲存空間不足
**風險**：大量下載導致下載失敗或使用者不滿
**對策**：
- 預設保守下載策略
- 提供容量限制與清理規則
- 下載前預檢空間

### 11.4 使用者不喜歡自動恢復播放
**風險**：通話結束後突然播放造成尷尬
**對策**：
- 僅在符合條件時恢復
- 提供設定開關
- 記錄 interruption 前狀態

---

## 12. 開發里程碑建議

### Phase 1：播放器骨架
- 建立 MediaSessionService
- 完成單一播放器與通知控制
- 完成 Radio / Podcast 基本播放

### Phase 2：Podcast 資料流
- RSS 訂閱與解析
- Room 結構完成
- Feed 更新與集數列表

### Phase 3：下載與自動清理
- DownloadManager / DownloadService 串接
- WorkManager 定期刷新
- 自動下載 / 自動刪除規則落地

### Phase 4：穩定性補強
- 重試與 reconnect 狀態機
- 音訊焦點與 interruption 規則
- 耳機/藍牙/來電測試

### Phase 5：UX 打磨
- 設定頁優化
- 首次使用引導
- 錯誤提示與 retry UX

---

## 13. 建議預設值

- 自動下載：開
- 自動下載網路條件：僅 Wi‑Fi
- 每個節目最多保留下載：5 集
- 已播放下載刪除：24 小時後
- 通話後自動恢復：開
- 恢復播放時間門檻：60 分鐘內
- 廣播自動重試：開
- 手動重試按鈕：顯示

---

## 14. 驗收清單

### 播放驗收
- [ ] 廣播可在背景穩定播放 30 分鐘以上
- [ ] Podcast 已下載內容在飛航模式可完整播放
- [ ] 通知列控制可正常播放/暫停/下一集

### 中斷驗收
- [ ] 來電時自動暫停
- [ ] 掛電話後在符合條件下恢復播放
- [ ] 耳機拔除時暫停
- [ ] 音訊焦點被其他 App 搶走時可正確回應

### 下載驗收
- [ ] 符合條件時自動下載最新集數
- [ ] 空間不足時能清理舊檔後重試
- [ ] 已播放集數可依規則自動刪除

### 網路驗收
- [ ] 廣播在 Wi‑Fi 中斷再恢復後可自動續播
- [ ] 廣播在 Wi‑Fi 與行動網路切換時可合理恢復
- [ ] Podcast 串流在弱網下能正常緩衝或提示

---

## 15. 一句話總結
這個 App 的 MVP 不是做最多功能，而是把「播放穩、斷了會回來、下載會自己管、來電不亂掉」這四件事先做到最好。

