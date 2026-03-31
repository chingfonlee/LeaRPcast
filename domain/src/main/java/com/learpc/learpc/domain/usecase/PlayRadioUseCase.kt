package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.mapper.RadioPlayableMapper
import javax.inject.Inject

class PlayRadioUseCase @Inject constructor(
    private val playbackController: PlaybackController,
    private val radioPlayableMapper: RadioPlayableMapper
) {
    operator fun invoke(station: RadioStation) {
        playbackController.setItem(radioPlayableMapper.toPlayableItem(station))
        playbackController.play()
    }
}
