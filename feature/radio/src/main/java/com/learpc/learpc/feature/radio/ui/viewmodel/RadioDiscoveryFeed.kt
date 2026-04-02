package com.learpc.learpc.feature.radio.ui.viewmodel

import com.learpc.learpc.core.model.radio.RadioStation
import java.util.Locale

internal data class RadioDiscoveryFeed(
    val featuredStations: List<RadioStation>,
    val moreStations: List<RadioStation>
)

internal data class RadioDiscoveryOptions(
    val typeOptions: List<String>,
    val regionOptions: List<String>
)

internal fun buildTaxonomyDiscoveryOptions(
    stations: List<RadioStation>,
    limit: Int = 4
): RadioDiscoveryOptions {
    val typeOptions = stations
        .asSequence()
        .mapNotNull { station ->
            station.uiPrimaryGroup?.trim()?.takeIf { value -> value.isNotBlank() }
                ?: station.category?.trim()?.takeIf { value -> value.isNotBlank() }
                ?: station.genre?.trim()?.takeIf { value -> value.isNotBlank() }
        }
        .groupBy { it.lowercase(Locale.ROOT) }
        .mapValues { entry -> entry.value.size to entry.value.first() }
        .entries
        .sortedWith(compareByDescending<Map.Entry<String, Pair<Int, String>>> { it.value.first }.thenBy { it.value.second.lowercase(Locale.ROOT) })
        .map { it.value.second }
        .take(limit)

    val regionOptions = stations
        .asSequence()
        .mapNotNull { station ->
            station.region?.trim()?.takeIf { value -> value.isNotBlank() }
                ?: station.country?.trim()?.takeIf { value -> value.isNotBlank() }
                ?: station.countryCode?.trim()?.takeIf { value -> value.isNotBlank() }
        }
        .groupBy { it.lowercase(Locale.ROOT) }
        .mapValues { entry -> entry.value.size to entry.value.first() }
        .entries
        .sortedWith(compareByDescending<Map.Entry<String, Pair<Int, String>>> { it.value.first }.thenBy { it.value.second.lowercase(Locale.ROOT) })
        .map { it.value.second }
        .take(limit)

    return RadioDiscoveryOptions(
        typeOptions = typeOptions,
        regionOptions = regionOptions
    )
}

internal fun buildTaxonomyDiscoveryFeed(
    stations: List<RadioStation>,
    locale: Locale = Locale.getDefault(),
    selectedType: String? = null,
    selectedRegion: String? = null,
    featuredLimit: Int = 4,
    moreLimit: Int = 4
): RadioDiscoveryFeed {
    val normalizedType = selectedType?.trim()?.takeIf { it.isNotBlank() }
    val normalizedRegion = selectedRegion?.trim()?.takeIf { it.isNotBlank() }

    val rankedStations = stations
        .filterNot { it.isFavorite }
        .sortedWith(
            compareByDescending<RadioStation> {
                matchesTaxonomyType(it, normalizedType)
            }.thenByDescending {
                matchesTaxonomyRegion(it, normalizedRegion)
            }.thenByDescending {
                val language = locale.language.takeIf { value -> value.isNotBlank() }
                language != null && it.language?.contains(language, ignoreCase = true) == true
            }.thenByDescending {
                val country = locale.country.takeIf { value -> value.isNotBlank() }
                country != null && it.countryCode?.equals(country, ignoreCase = true) == true
            }.thenByDescending {
                val displayCountry = locale.displayCountry.takeIf { value -> value.isNotBlank() }
                displayCountry != null && it.country?.contains(displayCountry, ignoreCase = true) == true
            }.thenByDescending {
                val language = locale.language.takeIf { value -> value.isNotBlank() }
                language != null && it.genre?.contains(language, ignoreCase = true) == true
            }.thenBy {
                it.sortOrder ?: Int.MAX_VALUE
            }.thenBy {
                it.name.lowercase(Locale.ROOT)
            }
        )

    val featuredStations = rankedStations.take(featuredLimit)
    val moreStations = rankedStations
        .drop(featuredStations.size)
        .take(moreLimit)

    return RadioDiscoveryFeed(
        featuredStations = featuredStations,
        moreStations = moreStations
    )
}

private fun matchesTaxonomyType(
    station: RadioStation,
    selectedType: String?
): Boolean {
    val normalizedType = selectedType?.trim()?.takeIf { it.isNotBlank() } ?: return true
    return station.uiPrimaryGroup?.contains(normalizedType, ignoreCase = true) == true ||
        station.category?.contains(normalizedType, ignoreCase = true) == true ||
        station.genre?.contains(normalizedType, ignoreCase = true) == true
}

private fun matchesTaxonomyRegion(
    station: RadioStation,
    selectedRegion: String?
): Boolean {
    val normalizedRegion = selectedRegion?.trim()?.takeIf { it.isNotBlank() } ?: return true
    return station.region?.contains(normalizedRegion, ignoreCase = true) == true ||
        station.country?.contains(normalizedRegion, ignoreCase = true) == true ||
        station.countryCode?.equals(normalizedRegion, ignoreCase = true) == true
}
