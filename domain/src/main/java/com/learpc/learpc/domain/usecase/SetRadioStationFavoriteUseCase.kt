package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import javax.inject.Inject

class SetRadioStationFavoriteUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(
        stationId: String,
        isFavorite: Boolean
    ): RadioStation {
        val now = System.currentTimeMillis()
        val existing = radioRepository.getById(stationId)
            ?: error("Radio station not found: $stationId")

        val updated = existing.copy(
            isFavorite = isFavorite,
            updatedAt = now
        )

        radioRepository.upsert(updated)
        return updated
    }
}
