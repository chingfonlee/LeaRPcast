package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.RadioStationEntity
import com.learpc.learpc.core.model.radio.RadioStation

object RadioStationMapper {
    fun RadioStationEntity.toDomain(): RadioStation {
        return RadioStation(
            id = stationId,
            name = name,
            streamUrl = streamUrl,
            resolvedStreamUrl = backupStreamUrl,
            homepageUrl = homepageUrl,
            artworkUrl = artworkUrl,
            country = country,
            countryCode = null,
            language = language,
            genre = genre,
            codec = codec,
            bitrateKbps = bitrateKbps,
            isFavorite = isFavorite,
            sortOrder = sortOrder,
            lastSyncedAt = lastSyncedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    fun RadioStation.toEntity(
        createdAt: Long = this.createdAt,
        updatedAt: Long = this.updatedAt,
        lastSyncedAt: Long = this.lastSyncedAt
    ): RadioStationEntity {
        return RadioStationEntity(
            stationId = id,
            name = name,
            streamUrl = streamUrl,
            backupStreamUrl = resolvedStreamUrl,
            homepageUrl = homepageUrl,
            artworkUrl = artworkUrl,
            country = country,
            language = language,
            genre = genre,
            codec = codec,
            bitrateKbps = bitrateKbps,
            isFavorite = isFavorite,
            sortOrder = sortOrder,
            lastSyncedAt = lastSyncedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
