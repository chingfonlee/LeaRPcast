package com.learpc.learpc.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learpc.learpc.core.database.dao.DownloadRecordDao
import com.learpc.learpc.core.database.dao.EpisodeDao
import com.learpc.learpc.core.database.dao.PlaybackProgressDao
import com.learpc.learpc.core.database.dao.PodcastDao
import com.learpc.learpc.core.database.dao.RadioStationDao
import com.learpc.learpc.core.database.entities.DownloadRecordEntity
import com.learpc.learpc.core.database.entities.EpisodeEntity
import com.learpc.learpc.core.database.entities.PodcastEntity
import com.learpc.learpc.core.database.entities.PlaybackProgressEntity
import com.learpc.learpc.core.database.entities.RadioStationEntity
import com.learpc.learpc.core.database.entities.SubscriptionSettingsEntity

@Database(
    entities = [
        RadioStationEntity::class,
        PodcastEntity::class,
        SubscriptionSettingsEntity::class,
        EpisodeEntity::class,
        PlaybackProgressEntity::class,
        DownloadRecordEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun playbackProgressDao(): PlaybackProgressDao
    abstract fun downloadRecordDao(): DownloadRecordDao
    abstract fun radioStationDao(): RadioStationDao
}
