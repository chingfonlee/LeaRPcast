package com.learpc.learpc.domain.mapper

import com.learpc.learpc.core.model.PlayableItem
import com.learpc.learpc.core.model.radio.RadioStation
import javax.inject.Inject

class RadioPlayableMapper @Inject constructor() {
    fun toPlayableItem(station: RadioStation): PlayableItem {
        return PlayableItem(
            id = station.id,
            title = station.name,
            subtitle = station.genre ?: station.country ?: station.language,
            imageUri = station.artworkUrl,
            mediaUri = station.streamUrl
        )
    }
}
