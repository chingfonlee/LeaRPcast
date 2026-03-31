package com.learpc.learpc.core.database.di

import android.content.Context
import androidx.room.Room
import com.learpc.learpc.core.database.AppDatabase
import com.learpc.learpc.core.database.dao.DownloadRecordDao
import com.learpc.learpc.core.database.dao.EpisodeDao
import com.learpc.learpc.core.database.dao.PlaybackProgressDao
import com.learpc.learpc.core.database.dao.PodcastDao
import com.learpc.learpc.core.database.dao.RadioStationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "radio_podcast.db"
        ).build()
    }

    @Provides
    fun providePodcastDao(database: AppDatabase): PodcastDao = database.podcastDao()

    @Provides
    fun provideEpisodeDao(database: AppDatabase): EpisodeDao = database.episodeDao()

    @Provides
    fun providePlaybackProgressDao(database: AppDatabase): PlaybackProgressDao =
        database.playbackProgressDao()

    @Provides
    fun provideDownloadRecordDao(database: AppDatabase): DownloadRecordDao =
        database.downloadRecordDao()

    @Provides
    fun provideRadioStationDao(database: AppDatabase): RadioStationDao =
        database.radioStationDao()
}
