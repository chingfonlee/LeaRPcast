package com.learpc.learpc.app.data.repository

import com.learpc.learpc.core.database.dao.DownloadRecordDao
import com.learpc.learpc.core.database.mapper.DownloadRecordMapper.toDomain
import com.learpc.learpc.core.database.mapper.DownloadRecordMapper.toEntity
import com.learpc.learpc.core.model.download.DownloadRecord
import com.learpc.learpc.domain.repository.DownloadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultDownloadRepository @Inject constructor(
    private val downloadRecordDao: DownloadRecordDao
) : DownloadRepository {
    override fun observeDownloads(): Flow<List<DownloadRecord>> {
        return downloadRecordDao.observeAll().map { records ->
            records.map { it.toDomain() }
        }
    }

    override suspend fun getByEpisodeId(episodeId: String): DownloadRecord? {
        return downloadRecordDao.getByEpisodeId(episodeId)?.toDomain()
    }

    override suspend fun upsertRecord(record: DownloadRecord) {
        downloadRecordDao.upsert(record.toEntity())
    }

    override suspend fun delete(record: DownloadRecord) {
        downloadRecordDao.delete(record.toEntity())
    }
}
