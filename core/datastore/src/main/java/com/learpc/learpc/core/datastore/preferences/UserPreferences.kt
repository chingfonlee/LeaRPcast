package com.learpc.learpc.core.datastore.preferences

data class UserPreferences(
    val autoDownloadEnabled: Boolean = false,
    val autoDownloadWifiOnly: Boolean = true,
    val allowMeteredDownload: Boolean = false,
    val globalDownloadLimitPerPodcast: Int = 10,
    val downloadWhenChargingOnly: Boolean = false,
    val autoDeleteMode: AutoDeleteMode = AutoDeleteMode.NEVER,
    val autoDeleteAfterHours: Int = 24,
    val cleanupOnLowStorageEnabled: Boolean = false,
    val maxStorageMbForDownloads: Int = 1024,
    val defaultPlaybackSpeed: Float = 1.0f,
    val skipSilenceEnabled: Boolean = false,
    val resumeAfterCallEnabled: Boolean = true,
    val resumeAfterFocusGainEnabled: Boolean = true,
    val resumeTimeoutMinutes: Int = 10,
    val becomingNoisyAutoPauseEnabled: Boolean = true,
    val lastOpenedTab: AppTab = AppTab.RADIO,
    val miniPlayerEnabled: Boolean = true,
    val showReconnectStatus: Boolean = true,
    val showDownloadNotifications: Boolean = true,
    val radioAutoRetryEnabled: Boolean = true,
    val radioRetryMaxAttempts: Int = 3,
    val radioRetryBaseDelayMs: Long = 2_000L,
    val podcastDefaultAutoDownloadEnabled: Boolean = false,
    val podcastDefaultNotificationEnabled: Boolean = true,
    val podcastDefaultKeepDownloadedCount: Int = 3
)

enum class AutoDeleteMode(val code: Int) {
    NEVER(0),
    AFTER_PLAYED(1),
    AFTER_24_HOURS(2),
    AFTER_7_DAYS(3);

    companion object {
        fun fromCode(code: Int): AutoDeleteMode {
            return entries.firstOrNull { it.code == code } ?: NEVER
        }
    }
}

enum class AppTab(val key: String) {
    RADIO("RADIO"),
    PODCAST("PODCAST"),
    DOWNLOADS("DOWNLOADS"),
    SETTINGS("SETTINGS");

    companion object {
        fun fromKey(key: String): AppTab {
            return entries.firstOrNull { it.key == key } ?: RADIO
        }
    }
}
