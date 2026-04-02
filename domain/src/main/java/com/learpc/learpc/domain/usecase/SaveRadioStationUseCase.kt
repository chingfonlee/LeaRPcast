package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import javax.inject.Inject

class SaveRadioStationUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(station: RadioStation): RadioStation {
        val now = System.currentTimeMillis()
        val existing = radioRepository.getById(station.id)
        val nextSortOrder = radioRepository.getStations()
            .mapNotNull { it.sortOrder }
            .maxOrNull()
            ?.plus(1)
            ?: 0

        val saved = station.copy(
            isFavorite = true,
            sortOrder = existing?.sortOrder ?: station.sortOrder ?: nextSortOrder,
            lastSyncedAt = existing?.lastSyncedAt ?: now,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )

        radioRepository.upsert(saved)
        return saved
    }
}
