package com.learpc.learpc.domain.mapper

import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.radio.RadioStation
import javax.inject.Inject

class RadioPlayableMapper @Inject constructor() {
    fun toPlayableItem(station: RadioStation): PlayableItem {
        val primaryMediaUri = station.resolvedStreamUrl ?: station.streamUrl
        val fallbackMediaUri = when {
            primaryMediaUri == station.resolvedStreamUrl && station.streamUrl != primaryMediaUri -> station.streamUrl
            primaryMediaUri == station.streamUrl && station.resolvedStreamUrl != primaryMediaUri -> station.resolvedStreamUrl
            else -> null
        }

        return PlayableItem(
            id = station.id,
            title = station.name,
            subtitle = station.genre ?: station.country ?: station.language,
            imageUri = station.artworkUrl,
            mediaUri = primaryMediaUri,
            fallbackMediaUri = fallbackMediaUri
        )
    }
}
