package com.learpc.learpc.core.network.radio.source

import com.learpc.learpc.core.network.radio.RadioBrowserApiService
import com.learpc.learpc.core.network.radio.model.RemoteRadioStation
import org.json.JSONArray
import javax.inject.Inject

class RadioRemoteDataSource @Inject constructor(
    private val radioBrowserApiService: RadioBrowserApiService
) {
    suspend fun fetchTopStations(limit: Int): Result<List<RemoteRadioStation>> {
        return runCatching {
            val payload = radioBrowserApiService.getTopStations(limit)
            val jsonArray = JSONArray(payload)
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    val station = RemoteRadioStation.fromJson(item)
                    if (station.stationUuid.isNotBlank() && station.name.isNotBlank()) {
                        add(station)
                    }
                }
            }
        }
    }

    suspend fun searchStations(name: String, limit: Int = 25): Result<List<RemoteRadioStation>> {
        return runCatching {
            val payload = radioBrowserApiService.searchStations(name = name, limit = limit)
            val jsonArray = JSONArray(payload)
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    val station = RemoteRadioStation.fromJson(item)
                    if (station.stationUuid.isNotBlank() && station.name.isNotBlank()) {
                        add(station)
                    }
                }
            }
        }
    }
}
