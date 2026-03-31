package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.common.NetworkChecker
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.model.podcast.SubscriptionSettings
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluateAutoDownloadUseCaseTest {
    private val settingsRepository = mockk<SettingsRepository>()
    private val podcastRepository = mockk<PodcastRepository>()
    private val networkChecker = mockk<NetworkChecker>()

    private val useCase = EvaluateAutoDownloadUseCase(
        settingsRepository = settingsRepository,
        podcastRepository = podcastRepository,
        networkChecker = networkChecker
    )

    @Test
    fun `returns false when global auto download is disabled`() = runTest {
        coEvery { podcastRepository.getSubscriptionSettings("podcast-1") } returns SubscriptionSettings(
            podcastId = "podcast-1",
            autoDownloadEnabled = true
        )
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences(autoDownloadEnabled = false))

        assertFalse(useCase(sampleEpisode(), "podcast-1"))
    }

    @Test
    fun `returns false when podcast override disables auto download`() = runTest {
        coEvery { podcastRepository.getSubscriptionSettings("podcast-1") } returns SubscriptionSettings(
            podcastId = "podcast-1",
            autoDownloadEnabled = false
        )
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())

        assertFalse(useCase(sampleEpisode(), "podcast-1"))
    }

    @Test
    fun `returns false when wifi only is enabled on metered network`() = runTest {
        coEvery { podcastRepository.getSubscriptionSettings("podcast-1") } returns SubscriptionSettings(
            podcastId = "podcast-1",
            autoDownloadEnabled = true,
            wifiOnlyOverride = true
        )
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())
        every { networkChecker.isOnUnmeteredNetwork() } returns false

        assertFalse(useCase(sampleEpisode(), "podcast-1"))
    }

    @Test
    fun `returns true when all rules allow auto download`() = runTest {
        coEvery { podcastRepository.getSubscriptionSettings("podcast-1") } returns SubscriptionSettings(
            podcastId = "podcast-1",
            autoDownloadEnabled = true,
            wifiOnlyOverride = false
        )
        every { settingsRepository.observeSettings() } returns flowOf(samplePreferences())
        every { networkChecker.isOnUnmeteredNetwork() } returns true

        assertTrue(useCase(sampleEpisode(), "podcast-1"))
    }

    private fun sampleEpisode() = Episode(
        id = "episode-1",
        podcastId = "podcast-1",
        title = "Episode 1",
        mediaUrl = "https://example.com/episode-1.mp3"
    )

    private fun samplePreferences(
        autoDownloadEnabled: Boolean = true
    ) = UserPreferences(
        autoDownloadEnabled = autoDownloadEnabled,
        autoDownloadWifiOnly = true
    )
}
