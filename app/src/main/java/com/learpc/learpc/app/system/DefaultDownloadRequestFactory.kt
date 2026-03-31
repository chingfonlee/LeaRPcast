package com.learpc.learpc.app.system

import androidx.media3.exoplayer.offline.DownloadRequest
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.domain.usecase.DownloadRequestFactory
import javax.inject.Inject

class DefaultDownloadRequestFactory @Inject constructor() : DownloadRequestFactory {
    override fun create(episode: Episode): DownloadRequest {
        return DownloadRequest.Builder(
            episode.id,
            android.net.Uri.parse(episode.mediaUrl)
        )
            .setCustomCacheKey(episode.id)
            .setData(episode.id.toByteArray())
            .build()
    }
}
