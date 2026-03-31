package com.learpc.learpc.core.datastore.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val AUTO_DOWNLOAD_ENABLED = booleanPreferencesKey("auto_download_enabled")
    val AUTO_DOWNLOAD_WIFI_ONLY = booleanPreferencesKey("auto_download_wifi_only")
    val ALLOW_METERED_DOWNLOAD = booleanPreferencesKey("allow_metered_download")
    val GLOBAL_DOWNLOAD_LIMIT_PER_PODCAST = intPreferencesKey("global_download_limit_per_podcast")
    val DOWNLOAD_WHEN_CHARGING_ONLY = booleanPreferencesKey("download_when_charging_only")

    val AUTO_DELETE_MODE = intPreferencesKey("auto_delete_mode")
    val AUTO_DELETE_AFTER_HOURS = intPreferencesKey("auto_delete_after_hours")
    val CLEANUP_ON_LOW_STORAGE_ENABLED = booleanPreferencesKey("cleanup_on_low_storage_enabled")
    val MAX_STORAGE_MB_FOR_DOWNLOADS = intPreferencesKey("max_storage_mb_for_downloads")

    val DEFAULT_PLAYBACK_SPEED = floatPreferencesKey("default_playback_speed")
    val SKIP_SILENCE_ENABLED = booleanPreferencesKey("skip_silence_enabled")
    val RESUME_AFTER_CALL_ENABLED = booleanPreferencesKey("resume_after_call_enabled")
    val RESUME_AFTER_FOCUS_GAIN_ENABLED = booleanPreferencesKey("resume_after_focus_gain_enabled")
    val RESUME_TIMEOUT_MINUTES = intPreferencesKey("resume_timeout_minutes")
    val BECOMING_NOISY_AUTO_PAUSE_ENABLED = booleanPreferencesKey("becoming_noisy_auto_pause_enabled")

    val LAST_OPENED_TAB = stringPreferencesKey("last_opened_tab")
    val MINI_PLAYER_ENABLED = booleanPreferencesKey("mini_player_enabled")
    val SHOW_RECONNECT_STATUS = booleanPreferencesKey("show_reconnect_status")
    val SHOW_DOWNLOAD_NOTIFICATIONS = booleanPreferencesKey("show_download_notifications")

    val RADIO_AUTO_RETRY_ENABLED = booleanPreferencesKey("radio_auto_retry_enabled")
    val RADIO_RETRY_MAX_ATTEMPTS = intPreferencesKey("radio_retry_max_attempts")
    val RADIO_RETRY_BASE_DELAY_MS = longPreferencesKey("radio_retry_base_delay_ms")

    val PODCAST_DEFAULT_AUTO_DOWNLOAD_ENABLED =
        booleanPreferencesKey("podcast_default_auto_download_enabled")
    val PODCAST_DEFAULT_NOTIFICATION_ENABLED =
        booleanPreferencesKey("podcast_default_notification_enabled")
    val PODCAST_DEFAULT_KEEP_DOWNLOADED_COUNT =
        intPreferencesKey("podcast_default_keep_downloaded_count")
}
