# Project Structure Spec v1

## 專案名稱
Android 廣播與 Podcast 收聽 App

## 文件版本
v1.0

## 文件目的
本文件定義專案的模組結構、package 命名規則、檔案擺放原則與分層責任，作為後續 AI Agent 與人工開發共同遵守的實作規範。

本文件的目標不是只列出資料夾，而是回答以下問題：
- 哪些程式碼應放在哪個 module
- feature 與 core 要如何分工
- UI / domain / data 要如何擺放
- Service / Worker / Repository / DAO / Mapper 應該放哪裡
- 其他 AI 產生檔案時要遵守什麼命名與分層規則

本文件依據：
- PRD + 技術架構清單 + MVP 功能表
- Technical Design Spec v1
- Data Schema v1

---

# 1. 專案總體原則

## 1.1 架構風格
本專案採用：
- **Multi-module**
- **Feature-first + Layered internal structure**
- **Clean Architecture boundary**
- **Single playback service source of truth**

## 1.2 核心原則
1. **先切 module，再切 package。**
2. **feature module 只放自己的 UI、feature-specific ViewModel、feature-specific orchestration。**
3. **共用能力放 core，不重複實作。**
4. **商業規則放 domain，不放 UI module。**
5. **播放核心與媒體抽象放 core:media，不散落到 feature 中。**
6. **Room / DataStore / Retrofit 等基礎實作放 core data modules。**
7. **任何其他 AI 不得把 DAO、Entity、Retrofit service 直接塞進 feature module。**

## 1.3 不採用的做法
以下結構不建議：
- 全專案只有單一 `app` module
- 以 `ui/`, `data/`, `domain/` 作為全域唯一分層，導致 feature 邊界模糊
- 在 feature 裡直接操作 Room DAO
- 在 Compose UI 裡直接調用 ExoPlayer
- 在多處建立不同 player instance

---

# 2. Module Structure

## 2.1 推薦 module 樹

```text
app/
core/
  common/
  ui/
  network/
  database/
  datastore/
  media/
  model/
  testing/
domain/
feature/
  radio/
  podcast/
  player/
  downloads/
  settings/
```

## 2.2 模組全名建議
若使用 Gradle module path，建議如下：

```text
:app
:core:common
:core:ui
:core:network
:core:database
:core:datastore
:core:media
:core:model
:core:testing
:domain
:feature:radio
:feature:podcast
:feature:player
:feature:downloads
:feature:settings
```

---

# 3. 每個 Module 的責任

## 3.1 `:app`
### 職責
- Application 類別
- Hilt entry point
- Navigation host
- App-level theme wiring
- 啟動頁 / root scaffold
- 將各 feature screen 組裝成完整 app

### 不應放入
- DAO
- Entity
- Repository implementation
- ExoPlayer 建立邏輯
- RSS parser 細節

---

## 3.2 `:core:common`
### 職責
- 共用常數
- `Result`, `AppError`, `DispatcherProvider`, `TimeProvider`
- sealed class / enum / extensions
- logging abstraction
- resource wrapper / utility

### 範例 package
```text
core/common/result/
core/common/error/
core/common/time/
core/common/dispatcher/
core/common/extensions/
core/common/logger/
```

---

## 3.3 `:core:ui`
### 職責
- 共用 Compose 元件
- theme
- design tokens
- loading / error / empty state 元件
- reusable player controls UI 元件

### 範例 package
```text
core/ui/theme/
core/ui/components/
core/ui/state/
core/ui/previews/
```

### 不應放入
- feature-specific business logic
- Repository 呼叫
- player service binding 細節

---

## 3.4 `:core:network`
### 職責
- Retrofit / OkHttp client
- interceptors
- feed fetching API
- radio station remote source API
- network DTO
- DTO mapper（remote -> intermediate data model）

### 範例 package
```text
core/network/di/
core/network/client/
core/network/rss/
core/network/radio/
core/network/model/
core/network/mapper/
```

### 不應放入
- Room entity
- domain model
- UI state

---

## 3.5 `:core:database`
### 職責
- Room database
- Entity
- DAO
- relation DTO
- migration
- local data source

### 範例 package
```text
core/database/di/
core/database/db/
core/database/entity/
core/database/dao/
core/database/relation/
core/database/migration/
core/database/source/
```

---

## 3.6 `:core:datastore`
### 職責
- Preferences DataStore
- key 定義
- serializer / wrapper
- UserPreferences data source

### 範例 package
```text
core/datastore/di/
core/datastore/preferences/
core/datastore/source/
core/datastore/model/
```

---

## 3.7 `:core:media`
### 職責
- ExoPlayer factory
- MediaItem builder
- Audio attributes
- MediaSession helpers
- playback event adapter
- reconnect policy
- interruption policy helper
- service-facing media abstractions

### 範例 package
```text
core/media/player/
core/media/session/
core/media/model/
core/media/error/
core/media/policy/
core/media/mapper/
```

### 補充
這個 module 是本專案最重要的核心之一。任何與 Media3、ExoPlayer、audio focus、media session 高耦合的程式都優先考慮放這裡，而不是塞進 feature。

---

## 3.8 `:core:model`
### 職責
- 純 Kotlin 共用 model
- feature 之間共享的 UI-neutral model
- domain-friendly shared structures

### 範例 package
```text
core/model/playable/
core/model/podcast/
core/model/radio/
core/model/download/
core/model/user/
```

### 補充
若專案規模還不大，這個 module 可暫時合併進 `:domain`。但若 AI agent 會同時產生多個 feature，獨立出來較穩。

---

## 3.9 `:core:testing`
### 職責
- test fixtures
- fake repositories
- fake player event generators
- coroutine test rules
- shared test utilities

---

## 3.10 `:domain`
### 職責
- Repository interfaces
- UseCase
- domain model（若不放 `:core:model`）
- business rules evaluator

### 範例 package
```text
domain/repository/
domain/usecase/
domain/model/
domain/policy/
```

### 不應放入
- Android framework 類別
- Room DAO
- Retrofit service
- Compose UI

---

## 3.11 `:feature:radio`
### 職責
- Radio 列表 UI
- radio screen state
- feature-specific ViewModel
- 電台篩選、收藏互動
- 呼叫 play radio use case

### 內部分層建議
```text
feature/radio/navigation/
feature/radio/ui/
feature/radio/viewmodel/
feature/radio/model/
feature/radio/mapper/
```

---

## 3.12 `:feature:podcast`
### 職責
- podcast 訂閱列表
- podcast detail 頁
- episode list 頁
- podcast 搜尋/新增 RSS（若 v1 有做）
- feature-specific ViewModel

### 內部分層建議
```text
feature/podcast/navigation/
feature/podcast/ui/
feature/podcast/viewmodel/
feature/podcast/model/
feature/podcast/mapper/
```

---

## 3.13 `:feature:player`
### 職責
- full player screen
- mini player UI
- player controls UI state
- 觀察當前播放狀態

### 補充
- 此 module 處理的是 UI 與狀態呈現，不是播放器底層實作
- 不能直接 new ExoPlayer

---

## 3.14 `:feature:downloads`
### 職責
- 下載列表
- 下載狀態展示
- 手動下載/取消操作 UI
- 清理結果頁面或提示

---

## 3.15 `:feature:settings`
### 職責
- 播放偏好設定頁
- 自動下載/刪除規則設定頁
- resume after call 開關
- radio retry 設定

---

# 4. 建議實際 Package Root

假設 applicationId / base package 為：

```text
com.example.radiocast
```

則 package 建議如下：

```text
com.example.radiocast.app
com.example.radiocast.core.common
com.example.radiocast.core.ui
com.example.radiocast.core.network
com.example.radiocast.core.database
com.example.radiocast.core.datastore
com.example.radiocast.core.media
com.example.radiocast.core.model
com.example.radiocast.domain
com.example.radiocast.feature.radio
com.example.radiocast.feature.podcast
com.example.radiocast.feature.player
com.example.radiocast.feature.downloads
com.example.radiocast.feature.settings
```

---

# 5. Module 內部資料夾規則

## 5.1 Feature Module 標準模板
每個 feature module 原則上使用以下結構：

```text
feature/<name>/
  src/main/java/.../
    navigation/
    ui/
      screen/
      components/
      state/
    viewmodel/
    model/
    mapper/
    preview/
  src/main/res/
```

### 說明
- `navigation/`：route、destination、entry function
- `ui/screen/`：畫面主 Composable
- `ui/components/`：feature 專屬元件
- `ui/state/`：screen state / event / effect
- `viewmodel/`：ViewModel
- `model/`：feature-specific presentation model
- `mapper/`：domain -> presentation model mapper

## 5.2 Core Data Module 標準模板
以 `:core:database` 為例：

```text
core/database/
  src/main/java/.../
    db/
    entity/
    dao/
    relation/
    migration/
    source/
    di/
```

## 5.3 Domain Module 標準模板

```text
domain/
  src/main/java/.../
    model/
    repository/
    usecase/
    policy/
```

## 5.4 Core Media Module 標準模板

```text
core/media/
  src/main/java/.../
    player/
    session/
    service/
    model/
    mapper/
    error/
    policy/
    di/
```

### 補充
若 `PlaybackService` 放在 `:app` 造成相依過重，可將 service implementation 放在 `:core:media:service` 對應 package，再由 `:app` 進行 manifest 與啟動整合。

---

# 6. Service / Worker / Receiver 擺放規則

## 6.1 PlaybackService
### 建議位置
優先兩種方案擇一：

### 方案 A（推薦）
- 類別實作放 `:core:media`
- AndroidManifest 註冊由 `:app` 處理

### 方案 B
- 類別直接放 `:app/service`
- 但其依賴邏輯仍應大部分抽到 `:core:media`

### 不建議
- 把 `PlaybackService` 放在 `feature:player`

## 6.2 Worker
Workers 建議放在最接近其責任的模組：

- `FeedRefreshWorker`：可放 `:feature:podcast` 或 `:app/work`
- `AutoCleanupWorker`：可放 `:feature:downloads` 或 `:app/work`

### 規則
若 Worker 僅是 orchestration layer，可放 `:app/work`；
若 Worker 與某 feature 高度綁定，也可放 feature module。

本專案建議 v1 採：
```text
app/work/
  FeedRefreshWorker.kt
  AutoCleanupWorker.kt
```

原因：
- WorkManager 多屬 app-level scheduling
- 有利統一管理 background task

## 6.3 BroadcastReceiver / Noisy Receiver
- 若使用系統廣播處理耳機拔除等事件，接收器建議放 `:app/receiver`
- 真正的處理邏輯抽到 `:core:media`

---

# 7. Repository / DataSource / Mapper 擺放規則

## 7.1 Repository Interface
放在 `:domain/repository`

## 7.2 Repository Implementation
建議放在最接近資料來源整合的位置，通常是 `:core` data modules 或 `:app/data`

本專案建議：
- 若 repository 實作單純由 local/remote/data store 組成，可放 `:app/data/repository`
- 若希望更嚴格模組化，可拆到 `:core:data`，但 v1 先不強制新增此 module

### v1 實務建議
由於目前 module 未單獨建立 `:data`，建議在 `:app` 中建立：

```text
app/data/repository/
app/data/mapper/
app/di/
```

### 原因
- 可以先避免 module 過度膨脹
- 保留未來把 repository implementation 抽成 `:data` module 的空間

## 7.3 Local / Remote DataSource
建議位置：

```text
app/data/source/local/
app/data/source/remote/
app/data/source/preferences/
```

## 7.4 Mapper
### 規則
- Remote DTO -> local intermediary：放 `:core:network/mapper`
- Entity -> domain：放 `app/data/mapper`
- Domain -> presentation：放各 feature 的 `mapper/`

---

# 8. 命名規則

## 8.1 類別命名
- Entity：`PodcastEntity`
- Dao：`PodcastDao`
- Repository interface：`PodcastRepository`
- Repository implementation：`DefaultPodcastRepository`
- Local data source：`PodcastLocalDataSource`
- Remote data source：`PodcastRemoteDataSource`
- UseCase：`PlayEpisodeUseCase`
- ViewModel：`PodcastViewModel`
- UI state：`PodcastUiState`
- UI event：`PodcastUiEvent`
- UI effect：`PodcastUiEffect`
- Mapper：`EpisodeUiMapper`

## 8.2 檔名規則
一個主要 public 類別對應一個檔案，避免把多個重要類別塞在同一檔。

## 8.3 package 命名
全部小寫，避免底線與縮寫混亂：
- `viewmodel`
- `navigation`
- `components`
- `repository`
- `datasource` 不建議，統一用 `source`

---

# 9. 建議實際檔案樹（v1）

```text
app/
  src/main/java/com/example/radiocast/app/
    RadioCastApplication.kt
    MainActivity.kt
    navigation/
      AppNavHost.kt
      AppDestinations.kt
    di/
      AppModule.kt
      RepositoryModule.kt
      WorkerModule.kt
    data/
      repository/
        DefaultRadioRepository.kt
        DefaultPodcastRepository.kt
        DefaultEpisodeRepository.kt
        DefaultPlaybackRepository.kt
        DefaultDownloadRepository.kt
        DefaultSettingsRepository.kt
      mapper/
        PodcastEntityMapper.kt
        EpisodeEntityMapper.kt
      source/
        local/
          RadioLocalDataSource.kt
          PodcastLocalDataSource.kt
          EpisodeLocalDataSource.kt
          DownloadLocalDataSource.kt
        remote/
          RadioRemoteDataSource.kt
          PodcastRemoteDataSource.kt
        preferences/
          SettingsPreferencesDataSource.kt
    work/
      FeedRefreshWorker.kt
      AutoCleanupWorker.kt
    receiver/
      AudioBecomingNoisyReceiver.kt

core/common/
  src/main/java/com/example/radiocast/core/common/
    result/
    error/
    time/
    dispatcher/
    logger/
    extensions/

core/ui/
  src/main/java/com/example/radiocast/core/ui/
    theme/
    components/
    state/

core/network/
  src/main/java/com/example/radiocast/core/network/
    di/
    client/
    rss/
      PodcastFeedService.kt
      FeedParser.kt
      model/
    radio/
      RadioApiService.kt
      model/
    mapper/

core/database/
  src/main/java/com/example/radiocast/core/database/
    db/
      AppDatabase.kt
    entity/
      RadioStationEntity.kt
      PodcastEntity.kt
      SubscriptionEntity.kt
      EpisodeEntity.kt
      PlaybackProgressEntity.kt
      DownloadRecordEntity.kt
      QueueItemEntity.kt
      RecentPlayEntity.kt
    dao/
      RadioStationDao.kt
      PodcastDao.kt
      SubscriptionDao.kt
      EpisodeDao.kt
      PlaybackProgressDao.kt
      DownloadRecordDao.kt
      QueueDao.kt
      RecentPlayDao.kt
    relation/
      PodcastWithSubscription.kt
      EpisodeWithPlaybackAndDownload.kt
      PodcastDetailAggregate.kt
    migration/
      Migration1To2.kt
    di/
      DatabaseModule.kt

core/datastore/
  src/main/java/com/example/radiocast/core/datastore/
    preferences/
      UserPreferences.kt
      PreferenceKeys.kt
    source/
      UserPreferencesDataSource.kt
    di/
      DataStoreModule.kt

core/media/
  src/main/java/com/example/radiocast/core/media/
    player/
      PlayerFactory.kt
      PlaybackController.kt
    service/
      PlaybackService.kt
    session/
      MediaSessionFactory.kt
      PlaybackSessionCallback.kt
    model/
      PlayableItem.kt
      PlaybackStateModel.kt
      PlaybackError.kt
      InterruptionSnapshot.kt
    policy/
      RadioReconnectPolicy.kt
      ResumeAfterInterruptionPolicy.kt
      PlaybackErrorClassifier.kt
    mapper/
      PlayerEventMapper.kt
    di/
      MediaModule.kt

core/model/
  src/main/java/com/example/radiocast/core/model/
    radio/
      RadioStation.kt
    podcast/
      Podcast.kt
      Episode.kt
    download/
      DownloadStatus.kt
    playback/
      PlaybackSnapshot.kt

core/testing/
  src/test/java/com/example/radiocast/core/testing/
    fake/
    rule/
    util/

domain/
  src/main/java/com/example/radiocast/domain/
    repository/
      RadioRepository.kt
      PodcastRepository.kt
      EpisodeRepository.kt
      PlaybackRepository.kt
      DownloadRepository.kt
      SettingsRepository.kt
    usecase/
      PlayRadioUseCase.kt
      PlayEpisodeUseCase.kt
      PausePlaybackUseCase.kt
      StopPlaybackUseCase.kt
      RefreshPodcastFeedUseCase.kt
      EnqueueEpisodeDownloadUseCase.kt
      CleanupDownloadsUseCase.kt
      ResumeAfterInterruptionUseCase.kt
    policy/
      CleanupEligibilityEvaluator.kt
      AutoDownloadEvaluator.kt

feature/radio/
  src/main/java/com/example/radiocast/feature/radio/
    navigation/
      RadioNavigation.kt
    ui/
      screen/
        RadioScreen.kt
      components/
        RadioStationCard.kt
      state/
        RadioUiState.kt
        RadioUiEvent.kt
    viewmodel/
      RadioViewModel.kt
    mapper/
      RadioUiMapper.kt

feature/podcast/
  src/main/java/com/example/radiocast/feature/podcast/
    navigation/
      PodcastNavigation.kt
    ui/
      screen/
        PodcastListScreen.kt
        PodcastDetailScreen.kt
        EpisodeListScreen.kt
      components/
        PodcastCard.kt
        EpisodeRow.kt
      state/
        PodcastUiState.kt
        PodcastUiEvent.kt
    viewmodel/
      PodcastViewModel.kt
      PodcastDetailViewModel.kt
    mapper/
      PodcastUiMapper.kt
      EpisodeUiMapper.kt

feature/player/
  src/main/java/com/example/radiocast/feature/player/
    navigation/
      PlayerNavigation.kt
    ui/
      screen/
        PlayerScreen.kt
      components/
        MiniPlayer.kt
        PlayerControls.kt
      state/
        PlayerUiState.kt
        PlayerUiEvent.kt
    viewmodel/
      PlayerViewModel.kt
    mapper/
      PlayerUiMapper.kt

feature/downloads/
  src/main/java/com/example/radiocast/feature/downloads/
    navigation/
      DownloadsNavigation.kt
    ui/
      screen/
        DownloadsScreen.kt
      components/
        DownloadItem.kt
      state/
        DownloadsUiState.kt
    viewmodel/
      DownloadsViewModel.kt
    mapper/
      DownloadsUiMapper.kt

feature/settings/
  src/main/java/com/example/radiocast/feature/settings/
    navigation/
      SettingsNavigation.kt
    ui/
      screen/
        SettingsScreen.kt
      components/
        SettingsGroup.kt
      state/
        SettingsUiState.kt
        SettingsUiEvent.kt
    viewmodel/
      SettingsViewModel.kt
```

---

# 10. AI Agent 實作規則

## 10.1 其他 AI 必須遵守
1. 新檔案必須放在本文件指定的 module 與 package。
2. 不可直接在 feature 中建立 DAO、Entity、Retrofit service。
3. 不可在 UI Composable 中直接呼叫 repository implementation。
4. 不可在 feature module 中直接建立 ExoPlayer。
5. 若新增共用元件，先判斷是否應放 `core:ui`。
6. 若新增共用 business rule，先判斷是否應放 `domain/policy`。
7. 若新增播放相關 policy，優先放 `core:media/policy`。

## 10.2 其他 AI 若遇到模糊情況，判斷順序
先問自己：
1. 這是 Android framework / app wiring 嗎？→ `:app`
2. 這是跨 feature 共用 UI 嗎？→ `:core:ui`
3. 這是資料庫或 DataStore 嗎？→ `:core:database` / `:core:datastore`
4. 這是 Media3 / player / audio focus / session 嗎？→ `:core:media`
5. 這是純商業規則或 use case 嗎？→ `:domain`
6. 這是某個畫面專用 UI 邏輯嗎？→ 對應 `:feature:*`

---

# 11. v1 開發時的簡化策略

若一開始覺得 module 太多，可做以下「不破壞架構方向」的簡化：

## 11.1 可接受簡化
- `:core:model` 暫時併入 `:domain/model`
- `:core:testing` 先不獨立 module
- Repository implementation 暫時集中在 `:app/data`

## 11.2 不可簡化
- 不可取消 `:core:media`
- 不可取消 `:core:database`
- 不可把所有 feature 合回單一 feature package
- 不可讓 UI 直接碰 DAO 或 ExoPlayer

---

# 12. 後續文件依賴關係
本文件完成後，接下來最適合產出的文件為：

1. **Issue List / 開發任務清單**
2. **AI Agent Execution Pack**
3. **DAO 介面草稿**
4. **Repository skeleton spec**

因為現在：
- 技術邊界已由 TDS 定義
- 資料邊界已由 Data Schema 定義
- 程式擺放位置已由本文件定義

此時已可安全拆成可執行 task。

---

# 13. v1 結論
本專案的推薦結構是：
- `:app` 負責組裝與 app-level wiring
- `:core:*` 負責共用能力與基礎設施
- `:domain` 負責商業規則與 use case
- `:feature:*` 負責畫面與 feature-specific presentation logic

此結構的目的不是追求形式上的模組化，而是讓多個 AI Agent 同時工作時，仍能維持：
- 責任清楚
- 檔案位置一致
- 相依方向穩定
- 後續重構成本可控

這份 Project Structure Spec v1 可直接作為 AI 開發代理的檔案擺放與命名規範。

