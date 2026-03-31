package com.learpc.learpc.core.media

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.offline.DownloadManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject
import okhttp3.OkHttpClient

@UnstableApi
class DownloadManagerProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val databaseProvider = StandaloneDatabaseProvider(context)
    private val downloadCacheDir: File by lazy {
        val baseDir = context.getExternalFilesDir(null) ?: context.filesDir
        File(baseDir, "podcast_download_cache").apply {
            mkdirs()
        }
    }
    private val downloadCache = SimpleCache(
        downloadCacheDir,
        NoOpCacheEvictor(),
        databaseProvider
    )
    private val upstreamFactory = OkHttpDataSource.Factory(OkHttpClient())
    private val downloadExecutor = Executor(Runnable::run)

    val downloadManager: DownloadManager by lazy {
        DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            upstreamFactory,
            downloadExecutor
        )
    }
}
