package com.learpc.learpc.domain.repository

import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<UserPreferences>
    suspend fun setWifiOnlyDownload(enabled: Boolean)
    suspend fun setAutoDeleteMode(autoDeleteMode: AutoDeleteMode)
    suspend fun setResumeAfterCall(enabled: Boolean)
    suspend fun setDefaultPlaybackSpeed(playbackSpeed: Float)
    suspend fun setRadioRetryEnabled(enabled: Boolean)
    suspend fun setRadioMaxRetries(maxRetries: Int)
}
