package com.learpc.learpc.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.usecase.EnqueueEpisodeDownloadUseCase
import com.learpc.learpc.domain.usecase.EvaluateAutoDownloadUseCase
import com.learpc.learpc.domain.usecase.RefreshPodcastFeedUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.flow.first

@HiltWorker
class FeedRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val podcastRepository: PodcastRepository,
    private val episodeRepository: EpisodeRepository,
    private val refreshPodcastFeedUseCase: RefreshPodcastFeedUseCase,
    private val evaluateAutoDownloadUseCase: EvaluateAutoDownloadUseCase,
    private val enqueueEpisodeDownloadUseCase: EnqueueEpisodeDownloadUseCase
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val podcasts = podcastRepository.observePodcasts().first()
            .filter { it.isActive }

        for (podcast in podcasts) {
            val existingEpisodeIds = episodeRepository.observeByPodcastId(podcast.id)
                .first()
                .map { it.id }
                .toSet()

            val refreshResult = refreshPodcastFeedUseCase(podcast.id, podcast.feedUrl)
            if (refreshResult.isFailure) {
                return if (refreshResult.exceptionOrNull().isRetryableNetworkFailure()) {
                    Result.retry()
                } else {
                    Result.failure()
                }
            }

            val refreshedEpisodes = episodeRepository.observeByPodcastId(podcast.id).first()
            val newEpisodes = refreshedEpisodes.filterNot { it.id in existingEpisodeIds }

            for (episode in newEpisodes) {
                if (evaluateAutoDownloadUseCase(episode, podcast.id)) {
                    enqueueEpisodeDownloadUseCase(episode)
                }
            }
        }

        return Result.success()
    }

    private fun Throwable?.isRetryableNetworkFailure(): Boolean {
        return when (this) {
            is IOException,
            is UnknownHostException,
            is SocketTimeoutException -> true
            else -> false
        }
    }
}
