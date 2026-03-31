package com.learpc.learpc.core.media.service

import android.app.Notification
import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import com.learpc.learpc.core.media.DownloadManagerProvider
import com.learpc.learpc.core.media.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PodcastDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.download_notification_channel_name,
    R.string.download_notification_channel_description
) {
    @Inject lateinit var downloadManagerProvider: DownloadManagerProvider

    override fun getDownloadManager(): DownloadManager {
        return downloadManagerProvider.downloadManager
    }

    override fun getScheduler(): Scheduler? = null

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val helper = DownloadNotificationHelper(this, CHANNEL_ID)
        return helper.buildProgressNotification(
            this,
            android.R.drawable.stat_sys_download,
            null,
            getString(R.string.download_notification_message),
            downloads,
            notMetRequirements
        )
    }

    private companion object {
        const val FOREGROUND_NOTIFICATION_ID = 2001
        const val CHANNEL_ID = "podcast_downloads"
    }
}
