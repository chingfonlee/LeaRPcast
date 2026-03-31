package com.learpc.learpc.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.learpc.learpc.domain.usecase.CleanupDownloadsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class AutoCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val cleanupDownloadsUseCase: CleanupDownloadsUseCase
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val cleanedCount = cleanupDownloadsUseCase()
        Timber.i("Auto cleanup completed, cleaned %d downloads", cleanedCount)
        return Result.success()
    }
}
