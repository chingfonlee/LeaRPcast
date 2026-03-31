package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.common.NetworkChecker
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class EvaluateAutoDownloadUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val podcastRepository: PodcastRepository,
    private val networkChecker: NetworkChecker
) {
    suspend operator fun invoke(episode: Episode, podcastId: String): Boolean {
        val preferences = settingsRepository.observeSettings().first()
        val subscriptionSettings = podcastRepository.getSubscriptionSettings(podcastId)

        val globalAutoDownloadEnabled = preferences.autoDownloadEnabled
        if (!globalAutoDownloadEnabled) return false

        val podcastAutoDownloadEnabled = subscriptionSettings?.autoDownloadEnabled
            ?: preferences.podcastDefaultAutoDownloadEnabled
        if (!podcastAutoDownloadEnabled) return false

        val wifiOnly = subscriptionSettings?.wifiOnlyOverride
            ?: preferences.autoDownloadWifiOnly
        if (wifiOnly && !networkChecker.isOnUnmeteredNetwork()) return false

        return true
    }
}
