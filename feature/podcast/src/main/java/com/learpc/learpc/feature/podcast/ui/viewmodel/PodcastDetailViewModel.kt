package com.learpc.learpc.feature.podcast.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.media.PlaybackController
import com.learpc.learpc.core.model.podcast.Episode
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.domain.repository.EpisodeRepository
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.usecase.PlayEpisodeUseCase
import com.learpc.learpc.feature.podcast.navigation.PODCAST_ID_ARG
import com.learpc.learpc.feature.podcast.ui.model.EpisodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PodcastDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    podcastRepository: PodcastRepository,
    episodeRepository: EpisodeRepository,
    private val playbackController: PlaybackController,
    private val playEpisodeUseCase: PlayEpisodeUseCase
) : ViewModel() {
    val podcastId: String = checkNotNull(savedStateHandle[PODCAST_ID_ARG])

    val podcast: StateFlow<Podcast?> = podcastRepository.observePodcasts()
        .map { podcasts -> podcasts.firstOrNull { it.id == podcastId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val episodeUiState: StateFlow<EpisodeUiState> = combine(
        episodeRepository.observeByPodcastId(podcastId),
        playbackController.currentItem
    ) { episodes, currentItem ->
        EpisodeUiState(
            isLoading = false,
            episodes = episodes,
            currentlyPlayingId = currentItem?.id
        )
    }
        .onStart { emit(EpisodeUiState(isLoading = true)) }
        .catch { throwable ->
            emit(
                EpisodeUiState(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Could not load episodes."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EpisodeUiState()
        )

    fun playEpisode(episode: Episode) {
        viewModelScope.launch {
            playEpisodeUseCase(episode)
        }
    }
}
