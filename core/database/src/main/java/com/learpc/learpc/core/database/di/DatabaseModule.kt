package com.learpc.learpc.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE radio_station ADD COLUMN display_name TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN display_frequency TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN frequency TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN band TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN source_group TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN network TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN region TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN category TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN media_type TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN ui_primary_group TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN ui_secondary_group TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN country_code TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN search_keywords_json TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN aliases_json TEXT")
            db.execSQL("ALTER TABLE radio_station ADD COLUMN merged_from_ids_json TEXT")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "radio_podcast.db"
        ).addMigrations(MIGRATION_1_2)
            .build()
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
