# Data Schema v1

## 專案名稱
Android 廣播與 Podcast 收聽 App

## 文件版本
v1.0

## 文件目的
本文件定義本專案的資料持久化設計，包含：
- Room database entity
- entity 關聯與索引策略
- DAO 邊界
- DataStore schema
- 命名規則與 migration 初始原則

本文件依據 `Technical Design Spec v1` 制定，作為後續 DAO、Repository、Download 狀態同步與設定頁實作的依據。

---

# 1. 設計原則

## 1.1 Room 適用範圍
Room 儲存：
- 需要查詢、排序、關聯的結構資料
- Podcast/集數/訂閱/下載/進度等核心業務資料
- 可離線使用且需長期保留的狀態

## 1.2 DataStore 適用範圍
DataStore 儲存：
- 使用者偏好設定
- 小型且不需複雜查詢的狀態
- App 層級預設值

## 1.3 命名原則
- Room entity 使用單數名稱
- 資料表名稱使用 snake_case
- 欄位名稱使用 snake_case
- Kotlin data class 屬性使用 camelCase
- 所有時間欄位一律使用 `Long` epoch millis
- 布林值欄位以 `is_` / `has_` 開頭

## 1.4 ID 原則
- 外部來源的穩定 ID 優先直接使用作為 business key
- Room 主鍵若需自行生成，可使用 String UUID
- Podcast episode 建議使用 feed URL + guid 組合出穩定 ID
- Radio station 建議使用來源提供 ID；若無則用 stream URL hash

---

# 2. Room Database 概覽

## 2.1 Database 名稱
`radio_podcast.db`

## 2.2 Room Entities 清單
1. `RadioStationEntity`
2. `PodcastEntity`
3. `SubscriptionEntity`
4. `EpisodeEntity`
5. `PlaybackProgressEntity`
6. `DownloadRecordEntity`
7. `QueueItemEntity`
8. `RecentPlayEntity`

## 2.3 可選後續 Entities（非 v1 必要）
- `EpisodeBookmarkEntity`
- `PodcastCategoryEntity`
- `SearchHistoryEntity`

---

# 3. Entity 詳細設計

## 3.1 RadioStationEntity

### 用途
儲存電台清單與本地快取資訊。

### Table
`radio_station`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "radio_station",
    indices = [
        Index(value = ["name"]),
        Index(value = ["country"]),
        Index(value = ["language"]),
        Index(value = ["genre"]),
        Index(value = ["is_favorite"])
    ]
)
data class RadioStationEntity(
    @PrimaryKey
    @ColumnInfo(name = "station_id")
    val stationId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "stream_url")
    val streamUrl: String,

    @ColumnInfo(name = "backup_stream_url")
    val backupStreamUrl: String? = null,

    @ColumnInfo(name = "homepage_url")
    val homepageUrl: String? = null,

    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,

    @ColumnInfo(name = "country")
    val country: String? = null,

    @ColumnInfo(name = "language")
    val language: String? = null,

    @ColumnInfo(name = "genre")
    val genre: String? = null,

    @ColumnInfo(name = "codec")
    val codec: String? = null,

    @ColumnInfo(name = "bitrate_kbps")
    val bitrateKbps: Int? = null,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "sort_order")
    val sortOrder: Int? = null,

    @ColumnInfo(name = "last_synced_at")
    val lastSyncedAt: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### 備註
- `backup_stream_url` 供未來容錯使用，v1 可先保留欄位不實作切換
- `is_favorite` 支援收藏功能，即使 MVP 尚未完整開 UI，也建議先留

---

## 3.2 PodcastEntity

### 用途
儲存 podcast 節目層級資料。

### Table
`podcast`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "podcast",
    indices = [
        Index(value = ["title"]),
        Index(value = ["author"]),
        Index(value = ["feed_url"], unique = true),
        Index(value = ["is_active"])
    ]
)
data class PodcastEntity(
    @PrimaryKey
    @ColumnInfo(name = "podcast_id")
    val podcastId: String,

    @ColumnInfo(name = "feed_url")
    val feedUrl: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "website_url")
    val websiteUrl: String? = null,

    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,

    @ColumnInfo(name = "language")
    val language: String? = null,

    @ColumnInfo(name = "copyright")
    val copyright: String? = null,

    @ColumnInfo(name = "explicit")
    val explicit: Boolean = false,

    @ColumnInfo(name = "last_feed_published_at")
    val lastFeedPublishedAt: Long? = null,

    @ColumnInfo(name = "last_refreshed_at")
    val lastRefreshedAt: Long? = null,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### 備註
- `PodcastEntity` 不直接表示使用者是否訂閱，訂閱狀態獨立於 `SubscriptionEntity`
- 這讓未來可支援搜尋結果暫存而不強制等於已訂閱

---

## 3.3 SubscriptionEntity

### 用途
儲存使用者對 podcast 的訂閱與節目級偏好覆寫。

### Table
`subscription`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "subscription",
    foreignKeys = [
        ForeignKey(
            entity = PodcastEntity::class,
            parentColumns = ["podcast_id"],
            childColumns = ["podcast_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["podcast_id"], unique = true),
        Index(value = ["is_subscribed"]),
        Index(value = ["auto_download_enabled"])
    ]
)
data class SubscriptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "podcast_id")
    val podcastId: String,

    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean = true,

    @ColumnInfo(name = "subscribed_at")
    val subscribedAt: Long,

    @ColumnInfo(name = "auto_download_enabled")
    val autoDownloadEnabled: Boolean = true,

    @ColumnInfo(name = "wifi_only_override")
    val wifiOnlyOverride: Boolean? = null,

    @ColumnInfo(name = "download_limit_override")
    val downloadLimitOverride: Int? = null,

    @ColumnInfo(name = "auto_delete_mode_override")
    val autoDeleteModeOverride: String? = null,

    @ColumnInfo(name = "playback_speed_override")
    val playbackSpeedOverride: Float? = null,

    @ColumnInfo(name = "skip_intro_seconds")
    val skipIntroSeconds: Int? = null,

    @ColumnInfo(name = "skip_outro_seconds")
    val skipOutroSeconds: Int? = null,

    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = false,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### 備註
- 這張表承接「節目級設定覆寫」
- 若欄位為 null，代表沿用 app-level DataStore 預設值

---

## 3.4 EpisodeEntity

### 用途
儲存 podcast 集數資料。

### Table
`episode`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "episode",
    foreignKeys = [
        ForeignKey(
            entity = PodcastEntity::class,
            parentColumns = ["podcast_id"],
            childColumns = ["podcast_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["podcast_id"]),
        Index(value = ["guid"]),
        Index(value = ["podcast_id", "published_at"]),
        Index(value = ["podcast_id", "is_played"]),
        Index(value = ["podcast_id", "is_archived"]),
        Index(value = ["podcast_id", "is_downloaded"]),
        Index(value = ["media_url"], unique = true)
    ]
)
data class EpisodeEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,

    @ColumnInfo(name = "podcast_id")
    val podcastId: String,

    @ColumnInfo(name = "guid")
    val guid: String? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "summary")
    val summary: String? = null,

    @ColumnInfo(name = "media_url")
    val mediaUrl: String,

    @ColumnInfo(name = "mime_type")
    val mimeType: String? = null,

    @ColumnInfo(name = "duration_ms")
    val durationMs: Long? = null,

    @ColumnInfo(name = "file_size_bytes")
    val fileSizeBytes: Long? = null,

    @ColumnInfo(name = "published_at")
    val publishedAt: Long? = null,

    @ColumnInfo(name = "artwork_url")
    val artworkUrl: String? = null,

    @ColumnInfo(name = "season_number")
    val seasonNumber: Int? = null,

    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int? = null,

    @ColumnInfo(name = "explicit")
    val explicit: Boolean = false,

    @ColumnInfo(name = "is_played")
    val isPlayed: Boolean = false,

    @ColumnInfo(name = "played_at")
    val playedAt: Long? = null,

    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,

    @ColumnInfo(name = "archived_at")
    val archivedAt: Long? = null,

    @ColumnInfo(name = "is_downloaded")
    val isDownloaded: Boolean = false,

    @ColumnInfo(name = "is_downloading")
    val isDownloading: Boolean = false,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "keep_download")
    val keepDownload: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### 備註
- `is_downloaded` 與 `DownloadRecordEntity` 會有重疊，但用途不同：
  - `EpisodeEntity.isDownloaded`：快速列表查詢用快取欄位
  - `DownloadRecordEntity`：下載細節與狀態來源
- `media_url` 設唯一索引可減少重複集數，但若遇到來源異常，仍應以 app 層 dedupe 邏輯輔助

---

## 3.5 PlaybackProgressEntity

### 用途
儲存 podcast 播放進度與播放完成資訊。

### Table
`playback_progress`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "playback_progress",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["episode_id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["episode_id"], unique = true),
        Index(value = ["last_played_at"]),
        Index(value = ["completion_state"])
    ]
)
data class PlaybackProgressEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,

    @ColumnInfo(name = "position_ms")
    val positionMs: Long,

    @ColumnInfo(name = "duration_ms")
    val durationMs: Long? = null,

    @ColumnInfo(name = "completion_state")
    val completionState: String,

    @ColumnInfo(name = "play_count")
    val playCount: Int = 0,

    @ColumnInfo(name = "last_played_at")
    val lastPlayedAt: Long? = null,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### `completion_state` 建議 enum 字串
- `NOT_STARTED`
- `IN_PROGRESS`
- `COMPLETED`

### 備註
- Radio 不寫入此表
- 對於完播判斷，建議 app 邏輯採「剩餘不足 N 秒即視為 completed」

---

## 3.6 DownloadRecordEntity

### 用途
儲存 episode 下載狀態與本地檔案資訊。

### Table
`download_record`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "download_record",
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = ["episode_id"],
            childColumns = ["episode_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["episode_id"], unique = true),
        Index(value = ["status"]),
        Index(value = ["downloaded_at"]),
        Index(value = ["local_file_uri"], unique = true)
    ]
)
data class DownloadRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    val episodeId: String,

    @ColumnInfo(name = "download_request_id")
    val downloadRequestId: String? = null,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "failure_reason")
    val failureReason: String? = null,

    @ColumnInfo(name = "bytes_downloaded")
    val bytesDownloaded: Long = 0,

    @ColumnInfo(name = "total_bytes")
    val totalBytes: Long? = null,

    @ColumnInfo(name = "local_file_uri")
    val localFileUri: String? = null,

    @ColumnInfo(name = "local_file_size_bytes")
    val localFileSizeBytes: Long? = null,

    @ColumnInfo(name = "download_started_at")
    val downloadStartedAt: Long? = null,

    @ColumnInfo(name = "downloaded_at")
    val downloadedAt: Long? = null,

    @ColumnInfo(name = "last_error_at")
    val lastErrorAt: Long? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
```

### `status` 建議 enum 字串
- `QUEUED`
- `DOWNLOADING`
- `COMPLETED`
- `FAILED`
- `REMOVING`
- `REMOVED`
- `STOPPED`

### 備註
- Media3 本身有 download index，但 app 仍建議維護一張可查詢的 app-level record table
- `local_file_uri` 需可對應實體檔是否存在，啟播前仍要做 file existence check

---

## 3.7 QueueItemEntity

### 用途
儲存播放佇列，供未來連播與佇列管理。

### Table
`queue_item`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "queue_item",
    indices = [
        Index(value = ["queue_type"]),
        Index(value = ["position_in_queue"]),
        Index(value = ["source_item_id"])
    ]
)
data class QueueItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "queue_item_id")
    val queueItemId: String,

    @ColumnInfo(name = "queue_type")
    val queueType: String,

    @ColumnInfo(name = "source_item_id")
    val sourceItemId: String,

    @ColumnInfo(name = "source_item_type")
    val sourceItemType: String,

    @ColumnInfo(name = "position_in_queue")
    val positionInQueue: Int,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
```

### `source_item_type`
- `EPISODE`
- `RADIO`

### 備註
- v1 可以只完整支援 episode queue
- radio queue 可先不開 UI，但 schema 先留不影響

---

## 3.8 RecentPlayEntity

### 用途
儲存最近播放紀錄，方便首頁與返回播放體驗。

### Table
`recent_play`

### Kotlin Schema
```kotlin
@Entity(
    tableName = "recent_play",
    indices = [
        Index(value = ["played_at"]),
        Index(value = ["item_type", "item_id"], unique = true)
    ]
)
data class RecentPlayEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "item_type")
    val itemType: String,

    @ColumnInfo(name = "item_id")
    val itemId: String,

    @ColumnInfo(name = "played_at")
    val playedAt: Long,
)
```

### `item_type`
- `RADIO`
- `EPISODE`

### 備註
- 使用 unique index 避免同一項目重複累積多筆紀錄
- app 層更新時採 upsert 並刷新 `played_at`

---

# 4. Entity Relations

## 4.1 Podcast 與 Subscription
- `PodcastEntity` 1 : 0..1 `SubscriptionEntity`

## 4.2 Podcast 與 Episode
- `PodcastEntity` 1 : N `EpisodeEntity`

## 4.3 Episode 與 PlaybackProgress
- `EpisodeEntity` 1 : 0..1 `PlaybackProgressEntity`

## 4.4 Episode 與 DownloadRecord
- `EpisodeEntity` 1 : 0..1 `DownloadRecordEntity`

## 4.5 Relation DTO 建議

### PodcastWithSubscription
```kotlin
data class PodcastWithSubscription(
    @Embedded val podcast: PodcastEntity,
    @Relation(
        parentColumn = "podcast_id",
        entityColumn = "podcast_id"
    )
    val subscription: SubscriptionEntity?
)
```

### EpisodeWithPlaybackAndDownload
```kotlin
data class EpisodeWithPlaybackAndDownload(
    @Embedded val episode: EpisodeEntity,
    @Relation(
        parentColumn = "episode_id",
        entityColumn = "episode_id"
    )
    val playback: PlaybackProgressEntity?,
    @Relation(
        parentColumn = "episode_id",
        entityColumn = "episode_id"
    )
    val download: DownloadRecordEntity?
)
```

### PodcastDetailAggregate
```kotlin
data class PodcastDetailAggregate(
    @Embedded val podcast: PodcastEntity,
    @Relation(
        parentColumn = "podcast_id",
        entityColumn = "podcast_id"
    )
    val subscription: SubscriptionEntity?,
    @Relation(
        parentColumn = "podcast_id",
        entityColumn = "podcast_id"
    )
    val episodes: List<EpisodeEntity>
)
```

---

# 5. DAO 邊界建議

## 5.1 RadioStationDao
### 主要操作
- upsert stations
- get all stations
- get favorite stations
- search by keyword
- update favorite state
- clear stale cache

## 5.2 PodcastDao
### 主要操作
- upsert podcast
- get podcast by id
- get podcast by feed url
- observe podcast list
- mark inactive

## 5.3 SubscriptionDao
### 主要操作
- upsert subscription
- get subscription by podcast id
- observe subscribed podcasts
- update per-podcast overrides
- unsubscribe

## 5.4 EpisodeDao
### 主要操作
- upsert episodes
- get episodes by podcast id order by publish date
- get episode by id
- mark played / archived / favorite
- update downloaded flags
- query cleanup candidates

## 5.5 PlaybackProgressDao
### 主要操作
- upsert progress
- get progress by episode id
- delete progress
- query recently played

## 5.6 DownloadRecordDao
### 主要操作
- upsert download record
- get by episode id
- observe active downloads
- query completed downloads
- query delete candidates
- delete record

## 5.7 QueueDao
### 主要操作
- insert queue items
- reorder queue
- remove queue item
- clear queue
- get current queue

## 5.8 RecentPlayDao
### 主要操作
- upsert recent item
- get recent items
- trim recent history

---

# 6. 查詢與索引策略

## 6.1 最常見查詢
1. 取得訂閱 podcast 清單
2. 取得某節目最新集數列表
3. 取得可繼續播放的 episode
4. 取得已下載內容
5. 取得清理候選項目
6. 取得最近播放
7. 取得收藏電台

## 6.2 索引重點
- `episode(podcast_id, published_at)`：集數列表
- `episode(podcast_id, is_downloaded)`：節目內下載查詢
- `download_record(status)`：下載頁與背景任務
- `playback_progress(last_played_at)`：最近播放排序
- `radio_station(is_favorite)`：收藏電台頁
- `subscription(is_subscribed)`：訂閱列表

## 6.3 避免過度索引
v1 不對 description / summary 建全文索引；搜尋可先做 title/author/name 的簡單查詢。

---

# 7. DataStore Schema

## 7.1 DataStore 類型
建議使用 `Preferences DataStore` 作為 v1 實作。

原因：
- 快速落地
- key-value 型設定足夠
- 無需先為每個偏好定 protobuf schema

若未來偏好設定成長，可在 v2 評估 Proto DataStore。

## 7.2 DataStore 檔名
`user_preferences.preferences_pb`

## 7.3 Key 清單

### 下載相關
- `auto_download_enabled` : Boolean
- `auto_download_wifi_only` : Boolean
- `allow_metered_download` : Boolean
- `global_download_limit_per_podcast` : Int
- `download_when_charging_only` : Boolean

### 刪除/清理相關
- `auto_delete_mode` : String
- `auto_delete_after_hours` : Int
- `cleanup_on_low_storage_enabled` : Boolean
- `max_storage_mb_for_downloads` : Int

### 播放相關
- `default_playback_speed` : Float
- `skip_silence_enabled` : Boolean
- `resume_after_call_enabled` : Boolean
- `resume_after_focus_gain_enabled` : Boolean
- `resume_timeout_minutes` : Int
- `becoming_noisy_auto_pause_enabled` : Boolean

### UI / UX 相關
- `last_opened_tab` : String
- `mini_player_enabled` : Boolean
- `show_reconnect_status` : Boolean
- `show_download_notifications` : Boolean

### 廣播相關
- `radio_auto_retry_enabled` : Boolean
- `radio_retry_max_attempts` : Int
- `radio_retry_base_delay_ms` : Long

### podcast 預設規則
- `podcast_default_auto_download_enabled` : Boolean
- `podcast_default_notification_enabled` : Boolean
- `podcast_default_keep_downloaded_count` : Int

---

# 8. DataStore 值域建議

## 8.1 `auto_delete_mode`
建議字串 enum：
- `NEVER`
- `AFTER_PLAYED`
- `AFTER_24_HOURS`
- `AFTER_7_DAYS`

## 8.2 `last_opened_tab`
- `RADIO`
- `PODCAST`
- `DOWNLOADS`
- `SETTINGS`

---

# 9. Domain Model Mapping 建議

## 9.1 Entity 不直接暴露給 UI
建議在 data 層建立 mapper：
- `RadioStationEntity -> RadioStation`
- `PodcastEntity + SubscriptionEntity -> Podcast`
- `EpisodeEntity + PlaybackProgressEntity + DownloadRecordEntity -> Episode`

## 9.2 Domain Model 範例
```kotlin
data class Episode(
    val id: String,
    val podcastId: String,
    val title: String,
    val mediaUrl: String,
    val localFileUri: String?,
    val publishedAt: Long?,
    val durationMs: Long?,
    val playbackPositionMs: Long,
    val isPlayed: Boolean,
    val isDownloaded: Boolean,
    val downloadStatus: DownloadStatus?,
    val keepDownload: Boolean,
)
```

---

# 10. Migration 初始原則

## 10.1 v1 目標
v1 先以 schema 穩定為優先，避免過早拆太細。

## 10.2 Migration 規則
- 所有 schema 變更都必須顯式 migration
- 不允許正式版依賴 destructive migration
- enum 字串值一旦上線，不應隨意更名
- 新增欄位優先使用 nullable 或有 default value

## 10.3 易變欄位提醒
以下欄位未來可能調整，設計時要注意兼容：
- `autoDeleteModeOverride`
- `completionState`
- `download status`
- `queue_type`
- `source_item_type`

---

# 11. 清理候選查詢建議

## 11.1 清理優先條件
刪除本地下載檔前，應交叉檢查：
- `episode.is_downloaded == true`
- `download_record.status == COMPLETED`
- `episode.keep_download == false`
- `episode.is_favorite == false`
- `episode.is_downloading == false`
- `episode.is_archived == true` 或 `episode.is_played == true`

## 11.2 代表性查詢需求
- 查出所有已完成下載且可安全刪除的 episode
- 依 `downloaded_at ASC` 或 `played_at ASC` 排序
- 依每個 podcast 的保留數量做超額清理

這部分建議 DAO 層先提供基礎查詢，複雜規則在 UseCase 層組合。

---

# 12. 推薦初版 RoomDatabase 宣告

```kotlin
@Database(
    entities = [
        RadioStationEntity::class,
        PodcastEntity::class,
        SubscriptionEntity::class,
        EpisodeEntity::class,
        PlaybackProgressEntity::class,
        DownloadRecordEntity::class,
        QueueItemEntity::class,
        RecentPlayEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun radioStationDao(): RadioStationDao
    abstract fun podcastDao(): PodcastDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun playbackProgressDao(): PlaybackProgressDao
    abstract fun downloadRecordDao(): DownloadRecordDao
    abstract fun queueDao(): QueueDao
    abstract fun recentPlayDao(): RecentPlayDao
}
```

---

# 13. v1 實作優先順序

## Phase A：最小可播放資料層
先做：
1. `PodcastEntity`
2. `SubscriptionEntity`
3. `EpisodeEntity`
4. `PlaybackProgressEntity`
5. `DownloadRecordEntity`
6. `RadioStationEntity`
7. 基本 DataStore keys

## Phase B：附加功能
再做：
1. `QueueItemEntity`
2. `RecentPlayEntity`
3. 進階設定 keys

---

# 14. 後續子工作
本文件之後，可直接衍生：

1. **DAO 介面草稿文件**
2. **Repository mapping 文件**
3. **專案資料夾結構文件**
4. **Issue List**

---

# 15. v1 結論
本 schema v1 的核心設計如下：
- **Room 負責結構資料與查詢**
- **DataStore 負責偏好設定與全域預設**
- **Episode / Playback / Download 三者分離**，避免播放、下載、完播狀態混在同一張表
- **Subscription 獨立於 Podcast**，保留未來搜尋/暫存擴充彈性
- **清理與自動下載規則採 app-level default + podcast-level override**

這份文件可以直接作為後續 DAO、Repository、migration 與 AI agent 實作資料層的依據。

