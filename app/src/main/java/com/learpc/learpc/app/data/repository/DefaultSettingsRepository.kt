package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.datastore.preferences.UserPreferencesDataSource
import com.learpc.learpc.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultSettingsRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) : SettingsRepository {
    override fun observeSettings(): Flow<UserPreferences> {
        return userPreferencesDataSource.observePreferences()
    }

    override suspend fun setWifiOnlyDownload(enabled: Boolean) {
        userPreferencesDataSource.setWifiOnlyDownload(enabled)
    }

    override suspend fun setAutoDeleteMode(autoDeleteMode: AutoDeleteMode) {
        userPreferencesDataSource.setAutoDeleteMode(autoDeleteMode)
    }

    override suspend fun setResumeAfterCall(enabled: Boolean) {
        userPreferencesDataSource.setResumeAfterCall(enabled)
    }

    override suspend fun setDefaultPlaybackSpeed(playbackSpeed: Float) {
        userPreferencesDataSource.setDefaultPlaybackSpeed(playbackSpeed)
    }

    override suspend fun setRadioRetryEnabled(enabled: Boolean) {
        userPreferencesDataSource.setRadioRetryEnabled(enabled)
    }

    override suspend fun setRadioMaxRetries(maxRetries: Int) {
        userPreferencesDataSource.setRadioMaxRetries(maxRetries)
    }
}
