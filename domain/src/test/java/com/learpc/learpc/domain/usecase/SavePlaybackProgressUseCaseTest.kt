package com.learpc.learpc.domain.usecase

import com.learpc.learpc.domain.repository.EpisodeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SavePlaybackProgressUseCaseTest {
    private val episodeRepository = mockk<EpisodeRepository>(relaxed = true)
    private val useCase = SavePlaybackProgressUseCase(episodeRepository)

    @Test
    fun `forwards progress update to repository`() = runTest {
        coEvery {
            episodeRepository.updateProgress("episode-1", 12_345L, false)
        } returns Unit

        useCase("episode-1", 12_345L, false)

        coVerify(exactly = 1) {
            episodeRepository.updateProgress("episode-1", 12_345L, false)
        }
    }

    @Test
    fun `forwards completion flag to repository`() = runTest {
        coEvery {
            episodeRepository.updateProgress("episode-1", 98_765L, true)
        } returns Unit

        useCase("episode-1", 98_765L, true)

        coVerify(exactly = 1) {
            episodeRepository.updateProgress("episode-1", 98_765L, true)
        }
    }
}
