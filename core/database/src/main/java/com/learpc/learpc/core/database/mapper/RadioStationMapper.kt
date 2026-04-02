package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.RadioStationEntity
import com.learpc.learpc.core.model.radio.RadioStation
import org.json.JSONArray

object RadioStationMapper {
    fun RadioStationEntity.toDomain(): RadioStation {
        return RadioStation(
            id = stationId,
            name = name,
            streamUrl = streamUrl,
            resolvedStreamUrl = backupStreamUrl,
            homepageUrl = homepageUrl,
            artworkUrl = artworkUrl,
            displayName = displayName,
            displayFrequency = displayFrequency,
            frequency = frequency,
            band = band,
            sourceGroup = sourceGroup,
            network = network,
            region = region,
            category = category,
            mediaType = mediaType,
            uiPrimaryGroup = uiPrimaryGroup,
            uiSecondaryGroup = uiSecondaryGroup,
            country = country,
            countryCode = countryCode,
            language = language,
            genre = genre,
            codec = codec,
            bitrateKbps = bitrateKbps,
            searchKeywords = searchKeywordsJson.fromJsonList(),
            aliases = aliasesJson.fromJsonList(),
            mergedFromIds = mergedFromIdsJson.fromJsonList(),
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
            displayName = displayName,
            displayFrequency = displayFrequency,
            frequency = frequency,
            band = band,
            sourceGroup = sourceGroup,
            network = network,
            region = region,
            category = category,
            mediaType = mediaType,
            uiPrimaryGroup = uiPrimaryGroup,
            uiSecondaryGroup = uiSecondaryGroup,
            country = country,
            countryCode = countryCode,
            language = language,
            genre = genre,
            codec = codec,
            bitrateKbps = bitrateKbps,
            searchKeywordsJson = searchKeywords.toJsonString(),
            aliasesJson = aliases.toJsonString(),
            mergedFromIdsJson = mergedFromIds.toJsonString(),
            isFavorite = isFavorite,
            sortOrder = sortOrder,
            lastSyncedAt = lastSyncedAt,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun String?.fromJsonList(): List<String> {
        val json = this?.takeIf { it.isNotBlank() } ?: return emptyList()
        return runCatching {
            val array = JSONArray(json)
            buildList {
                for (index in 0 until array.length()) {
                    val value = array.optString(index).trim()
                    if (value.isNotBlank()) {
                        add(value)
                    }
                }
            }
        }.getOrElse { emptyList() }
    }

    private fun List<String>.toJsonString(): String? {
        return if (isEmpty()) null else JSONArray(this).toString()
    }
}
