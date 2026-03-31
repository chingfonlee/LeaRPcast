package com.learpc.learpc.feature.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val settingsState: StateFlow<UserPreferences> = settingsRepository.observeSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences()
        )

    fun setWifiOnlyDownload(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setWifiOnlyDownload(enabled)
        }
    }

    fun setAutoDeleteMode(autoDeleteMode: AutoDeleteMode) {
        viewModelScope.launch {
            settingsRepository.setAutoDeleteMode(autoDeleteMode)
        }
    }

    fun setResumeAfterCall(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setResumeAfterCall(enabled)
        }
    }

    fun setDefaultPlaybackSpeed(playbackSpeed: Float) {
        viewModelScope.launch {
            settingsRepository.setDefaultPlaybackSpeed(playbackSpeed)
        }
    }

    fun setRadioRetryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRadioRetryEnabled(enabled)
        }
    }

    fun setRadioMaxRetries(maxRetries: Int) {
        viewModelScope.launch {
            settingsRepository.setRadioMaxRetries(maxRetries)
        }
    }
}
