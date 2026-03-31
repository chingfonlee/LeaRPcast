package com.learpc.learpc.domain.repository

import com.learpc.learpc.core.model.download.DownloadRecord
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun observeDownloads(): Flow<List<DownloadRecord>>
    suspend fun getByEpisodeId(episodeId: String): DownloadRecord?
    suspend fun upsertRecord(record: DownloadRecord)
    suspend fun delete(record: DownloadRecord)
}
