package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class AddRadioStationUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(draft: RadioStationDraft): RadioStation {
        val now = System.currentTimeMillis()
        val nextSortOrder = radioRepository.getStations()
            .mapNotNull { it.sortOrder }
            .maxOrNull()
            ?.plus(1)
            ?: 0

        val station = RadioStation(
            id = UUID.randomUUID().toString(),
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
            isFavorite = false,
            sortOrder = nextSortOrder,
            lastSyncedAt = now,
            createdAt = now,
            updatedAt = now
        )

        radioRepository.upsert(station)
        return station
    }
}
