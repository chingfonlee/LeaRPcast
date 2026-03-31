package com.learpc.learpc.domain.usecase

import androidx.media3.exoplayer.offline.DownloadRequest
import com.learpc.learpc.core.model.podcast.Episode

interface DownloadRequestFactory {
    fun create(episode: Episode): DownloadRequest
}
