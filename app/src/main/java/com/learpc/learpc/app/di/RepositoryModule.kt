package com.learpc.learpc.app.di

import com.learpc.learpc.app.data.repository.DefaultEpisodeRepository
import com.learpc.learpc.app.data.repository.DefaultDownloadRepository
import com.learpc.learpc.app.data.repository.DefaultPodcastRepository
import com.learpc.learpc.app.data.repository.DefaultRadioRepository
import com.learpc.learpc.app.data.repository.DefaultSettingsRepository
import com.learpc.learpc.app.system.DefaultDownloadRequestFactory
import com.learpc.learpc.core.media.PlaybackProgressSaver
import com.learpc.learpc.domain.repository.DownloadRepository
import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.repository.RadioRepository
import com.learpc.learpc.domain.repository.SettingsRepository
import com.learpc.learpc.domain.usecase.DownloadRequestFactory
import com.learpc.learpc.domain.usecase.SavePlaybackProgressUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
{
    @Binds
    @Singleton
    abstract fun bindPodcastRepository(
        defaultPodcastRepository: DefaultPodcastRepository
    ): PodcastRepository

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(
        defaultEpisodeRepository: DefaultEpisodeRepository
    ): EpisodeRepository

    @Binds
    @Singleton
    abstract fun bindDownloadRepository(
        defaultDownloadRepository: DefaultDownloadRepository
    ): DownloadRepository

    @Binds
    @Singleton
    abstract fun bindDownloadRequestFactory(
        defaultDownloadRequestFactory: DefaultDownloadRequestFactory
    ): DownloadRequestFactory

    @Binds
    @Singleton
    abstract fun bindRadioRepository(
        defaultRadioRepository: DefaultRadioRepository
    ): RadioRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        defaultSettingsRepository: DefaultSettingsRepository
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindPlaybackProgressSaver(
        savePlaybackProgressUseCase: SavePlaybackProgressUseCase
    ): PlaybackProgressSaver
}
