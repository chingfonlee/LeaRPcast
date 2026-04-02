package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.domain.mapper.RadioPlayableMapper
import com.learpc.learpc.domain.usecase.ResolveRadioStreamUseCase
import javax.inject.Inject

class PlayRadioUseCase @Inject constructor(
    private val playbackController: PlaybackController,
    private val radioPlayableMapper: RadioPlayableMapper,
    private val resolveRadioStreamUseCase: ResolveRadioStreamUseCase
) {
    suspend operator fun invoke(station: RadioStation) {
        val playableItem = radioPlayableMapper.toPlayableItem(station)
        val resolvedStream = resolveRadioStreamUseCase(station)
        playbackController.setItem(
            playableItem.copy(
                mediaUri = resolvedStream.mediaUri,
                fallbackMediaUri = resolvedStream.fallbackMediaUri
            )
        )
        playbackController.play()
    }
}
