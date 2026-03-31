package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class DeleteRadioStationUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(stationId: String) {
        val existing = radioRepository.getById(stationId) ?: return
        radioRepository.delete(existing)
        renumberStations(
            stations = radioRepository.getStations()
                .filterNot { it.id == stationId }
        )
    }

    private suspend fun renumberStations(stations: List<RadioStation>) {
        val now = System.currentTimeMillis()
        stations.forEachIndexed { index, station ->
            if (station.sortOrder != index) {
                radioRepository.upsert(
                    station.copy(
                        sortOrder = index,
                        updatedAt = now
                    )
                )
            }
        }
    }
}
