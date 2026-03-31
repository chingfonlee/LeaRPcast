package com.learpc.learpc.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.ALLOW_METERED_DOWNLOAD
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.AUTO_DELETE_AFTER_HOURS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.AUTO_DELETE_MODE
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.AUTO_DOWNLOAD_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.AUTO_DOWNLOAD_WIFI_ONLY
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.BECOMING_NOISY_AUTO_PAUSE_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.CLEANUP_ON_LOW_STORAGE_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.DEFAULT_PLAYBACK_SPEED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.DOWNLOAD_WHEN_CHARGING_ONLY
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.GLOBAL_DOWNLOAD_LIMIT_PER_PODCAST
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.LAST_OPENED_TAB
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.MAX_STORAGE_MB_FOR_DOWNLOADS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.MINI_PLAYER_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.PODCAST_DEFAULT_AUTO_DOWNLOAD_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.PODCAST_DEFAULT_KEEP_DOWNLOADED_COUNT
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.PODCAST_DEFAULT_NOTIFICATION_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RADIO_AUTO_RETRY_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RADIO_RETRY_BASE_DELAY_MS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RADIO_RETRY_MAX_ATTEMPTS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RESUME_AFTER_CALL_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RESUME_AFTER_FOCUS_GAIN_ENABLED
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.RESUME_TIMEOUT_MINUTES
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.SHOW_DOWNLOAD_NOTIFICATIONS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.SHOW_RECONNECT_STATUS
import com.learpc.learpc.core.datastore.preferences.PreferenceKeys.SKIP_SILENCE_ENABLED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun observePreferences(): Flow<UserPreferences> {
        return dataStore.data.map { preferences ->
            UserPreferences(
                autoDownloadEnabled = preferences[AUTO_DOWNLOAD_ENABLED] ?: false,
                autoDownloadWifiOnly = preferences[AUTO_DOWNLOAD_WIFI_ONLY] ?: true,
                allowMeteredDownload = preferences[ALLOW_METERED_DOWNLOAD] ?: false,
                globalDownloadLimitPerPodcast = preferences[GLOBAL_DOWNLOAD_LIMIT_PER_PODCAST] ?: 10,
                downloadWhenChargingOnly = preferences[DOWNLOAD_WHEN_CHARGING_ONLY] ?: false,
                autoDeleteMode = AutoDeleteMode.fromCode(preferences[AUTO_DELETE_MODE] ?: AutoDeleteMode.NEVER.code),
                autoDeleteAfterHours = preferences[AUTO_DELETE_AFTER_HOURS] ?: 24,
                cleanupOnLowStorageEnabled = preferences[CLEANUP_ON_LOW_STORAGE_ENABLED] ?: false,
                maxStorageMbForDownloads = preferences[MAX_STORAGE_MB_FOR_DOWNLOADS] ?: 1024,
                defaultPlaybackSpeed = preferences[DEFAULT_PLAYBACK_SPEED] ?: 1.0f,
                skipSilenceEnabled = preferences[SKIP_SILENCE_ENABLED] ?: false,
                resumeAfterCallEnabled = preferences[RESUME_AFTER_CALL_ENABLED] ?: true,
                resumeAfterFocusGainEnabled = preferences[RESUME_AFTER_FOCUS_GAIN_ENABLED] ?: true,
                resumeTimeoutMinutes = preferences[RESUME_TIMEOUT_MINUTES] ?: 10,
                becomingNoisyAutoPauseEnabled = preferences[BECOMING_NOISY_AUTO_PAUSE_ENABLED] ?: true,
                lastOpenedTab = AppTab.fromKey(preferences[LAST_OPENED_TAB] ?: AppTab.RADIO.key),
                miniPlayerEnabled = preferences[MINI_PLAYER_ENABLED] ?: true,
                showReconnectStatus = preferences[SHOW_RECONNECT_STATUS] ?: true,
                showDownloadNotifications = preferences[SHOW_DOWNLOAD_NOTIFICATIONS] ?: true,
                radioAutoRetryEnabled = preferences[RADIO_AUTO_RETRY_ENABLED] ?: true,
                radioRetryMaxAttempts = preferences[RADIO_RETRY_MAX_ATTEMPTS] ?: 3,
                radioRetryBaseDelayMs = preferences[RADIO_RETRY_BASE_DELAY_MS] ?: 2_000L,
                podcastDefaultAutoDownloadEnabled = preferences[PODCAST_DEFAULT_AUTO_DOWNLOAD_ENABLED] ?: false,
                podcastDefaultNotificationEnabled = preferences[PODCAST_DEFAULT_NOTIFICATION_ENABLED] ?: true,
                podcastDefaultKeepDownloadedCount = preferences[PODCAST_DEFAULT_KEEP_DOWNLOADED_COUNT] ?: 3
            )
        }
    }

    suspend fun setWifiOnlyDownload(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_DOWNLOAD_WIFI_ONLY] = enabled
        }
    }

    suspend fun setAutoDeleteMode(autoDeleteMode: AutoDeleteMode) {
        dataStore.edit { preferences ->
            preferences[AUTO_DELETE_MODE] = autoDeleteMode.code
        }
    }

    suspend fun setResumeAfterCall(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[RESUME_AFTER_CALL_ENABLED] = enabled
        }
    }

    suspend fun setDefaultPlaybackSpeed(playbackSpeed: Float) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_PLAYBACK_SPEED] = playbackSpeed
        }
    }

    suspend fun setRadioRetryEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[RADIO_AUTO_RETRY_ENABLED] = enabled
        }
    }

    suspend fun setRadioMaxRetries(maxRetries: Int) {
        dataStore.edit { preferences ->
            preferences[RADIO_RETRY_MAX_ATTEMPTS] = maxRetries
        }
    }
}
