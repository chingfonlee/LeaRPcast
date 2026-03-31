package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import javax.inject.Inject

class UpdateRadioStationUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(
        stationId: String,
        draft: RadioStationDraft
    ): RadioStation {
        val now = System.currentTimeMillis()
        val existing = radioRepository.getById(stationId)
            ?: error("Radio station not found: $stationId")

        val updated = existing.copy(
            name = draft.name,
            streamUrl = draft.streamUrl,
            resolvedStreamUrl = draft.resolvedStreamUrl,
            homepageUrl = draft.homepageUrl,
            artworkUrl = draft.artworkUrl,
            country = draft.country,
            countryCode = draft.countryCode,
            language = draft.language,
            genre = draft.genre,
            codec = draft.codec,
            bitrateKbps = draft.bitrateKbps,
            updatedAt = now
        )

        radioRepository.upsert(updated)
        return updated
    }
}
