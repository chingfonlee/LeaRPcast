package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.repository.RadioRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class ReorderRadioStationsUseCase @Inject constructor(
    private val radioRepository: RadioRepository
) {
    suspend operator fun invoke(stationId: String, targetIndex: Int) {
        val stations = radioRepository.getStations().toMutableList()
        val currentIndex = stations.indexOfFirst { it.id == stationId }
        if (currentIndex == -1) return

        val boundedTargetIndex = targetIndex.coerceIn(0, stations.lastIndex)
        if (currentIndex == boundedTargetIndex) return

        val station = stations.removeAt(currentIndex)
        stations.add(boundedTargetIndex, station)
        renumberStations(stations)
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
