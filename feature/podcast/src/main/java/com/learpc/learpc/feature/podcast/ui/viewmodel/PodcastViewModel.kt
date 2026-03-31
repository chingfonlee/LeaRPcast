package com.learpc.learpc.feature.podcast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learpc.learpc.core.model.podcast.Podcast
import com.learpc.learpc.domain.repository.PodcastRepository
import com.learpc.learpc.domain.usecase.RefreshPodcastFeedUseCase
import com.learpc.learpc.feature.podcast.ui.model.PodcastUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PodcastViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val refreshPodcastFeedUseCase: RefreshPodcastFeedUseCase
) : ViewModel() {
    val uiState: StateFlow<PodcastUiState> = podcastRepository.observePodcasts()
        .map { podcasts ->
            PodcastUiState(
                isLoading = false,
                podcasts = podcasts
            )
        }
        .onStart { emit(PodcastUiState(isLoading = true)) }
        .catch { throwable ->
            emit(
                PodcastUiState(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Could not load podcasts."
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PodcastUiState()
        )

    fun refreshPodcast(podcast: Podcast) {
        viewModelScope.launch {
            refreshPodcastFeedUseCase(podcast.id, podcast.feedUrl)
        }
    }
}
