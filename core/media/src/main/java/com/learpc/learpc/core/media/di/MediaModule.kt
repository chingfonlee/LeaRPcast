package com.learpc.learpc.core.media.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.SessionToken
import com.learpc.learpc.core.media.DefaultPlaybackController
import com.learpc.learpc.core.media.DownloadManagerProvider
import com.learpc.learpc.core.media.RadioReconnectPolicy
import com.learpc.learpc.core.media.factory.DefaultMediaSessionFactory
import com.learpc.learpc.core.media.factory.DefaultPlayerFactory
import com.learpc.learpc.core.media.factory.MediaSessionFactory
import com.learpc.learpc.core.media.factory.PlayerFactory
import com.learpc.learpc.core.media.service.PlaybackService
import androidx.media3.exoplayer.offline.DownloadManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaModule {

    @Binds
    @Singleton
    abstract fun bindPlayerFactory(
        defaultPlayerFactory: DefaultPlayerFactory
    ): PlayerFactory

    @Binds
    @Singleton
    abstract fun bindMediaSessionFactory(
        defaultMediaSessionFactory: DefaultMediaSessionFactory
    ): MediaSessionFactory

    @Binds
    @Singleton
    abstract fun bindPlaybackController(
        defaultPlaybackController: DefaultPlaybackController
    ): com.learpc.learpc.core.media.PlaybackController

    companion object {
        @Provides
        @Singleton
        fun provideDownloadManagerProvider(
            @ApplicationContext context: Context
        ): DownloadManagerProvider {
            return DownloadManagerProvider(context)
        }

        @Provides
        @Singleton
        fun provideDownloadManager(
            downloadManagerProvider: DownloadManagerProvider
        ): DownloadManager {
            return downloadManagerProvider.downloadManager
        }

        @Provides
        @Singleton
        fun provideRadioReconnectPolicy(): RadioReconnectPolicy {
            return RadioReconnectPolicy(
                maxAttempts = 5,
                baseDelayMs = 1_000L,
                maxDelayMs = 30_000L
            )
        }

        @Provides
        @Singleton
        fun providePlaybackSessionToken(
            @ApplicationContext context: Context
        ): SessionToken {
            return SessionToken(context, ComponentName(context, PlaybackService::class.java))
        }
    }
}
