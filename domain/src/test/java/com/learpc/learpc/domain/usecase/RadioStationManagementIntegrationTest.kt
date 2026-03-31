package com.learpc.learpc.domain.usecase

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.learpc.learpc.core.database.AppDatabase
import com.learpc.learpc.core.database.source.RadioLocalDataSource
import com.learpc.learpc.core.model.radio.RadioStation
import com.learpc.learpc.core.model.radio.RadioStationDraft
import com.learpc.learpc.domain.repository.RadioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class RadioStationManagementIntegrationTest {
    private lateinit var database: AppDatabase
    private lateinit var radioRepository: RadioRepository

    @Before
    fun setUp() {
        database = buildDatabase()
        radioRepository = PersistentRadioRepository(RadioLocalDataSource(database.radioStationDao()))
    }

    @After
    fun tearDown() {
        if (::database.isInitialized) {
            database.close()
        }
        ApplicationProvider.getApplicationContext<android.content.Context>()
            .deleteDatabase(TEST_DATABASE_NAME)
    }

    @Test
    fun `custom station changes survive database reopen`() = runTest {
        val addRadioStationUseCase = AddRadioStationUseCase(radioRepository)
        val updateRadioStationUseCase = UpdateRadioStationUseCase(radioRepository)
        val deleteRadioStationUseCase = DeleteRadioStationUseCase(radioRepository)
        val reorderRadioStationsUseCase = ReorderRadioStationsUseCase(radioRepository)

        val firstStation = addRadioStationUseCase(
            RadioStationDraft(
                name = "Morning FM",
                streamUrl = "https://example.com/morning.mp3",
                genre = "Talk"
            )
        )
        val secondStation = addRadioStationUseCase(
            RadioStationDraft(
                name = "Evening FM",
                streamUrl = "https://example.com/evening.mp3",
                genre = "Music"
            )
        )

        updateRadioStationUseCase(
            stationId = secondStation.id,
            draft = RadioStationDraft(
                name = "Evening FM Live",
                streamUrl = "https://example.com/evening-live.mp3",
                genre = "Live"
            )
        )
        reorderRadioStationsUseCase(secondStation.id, 0)
        deleteRadioStationUseCase(firstStation.id)

        database.close()
        database = buildDatabase()
        radioRepository = PersistentRadioRepository(RadioLocalDataSource(database.radioStationDao()))

        val persistedStations = radioRepository.getStations()
        assertEquals(1, persistedStations.size)
        assertEquals(secondStation.id, persistedStations.single().id)
        assertEquals("Evening FM Live", persistedStations.single().name)
        assertEquals(0, persistedStations.single().sortOrder)
    }

    private fun buildDatabase(): AppDatabase {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        return Room.databaseBuilder(context, AppDatabase::class.java, TEST_DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }

    private class PersistentRadioRepository(
        private val radioLocalDataSource: RadioLocalDataSource
    ) : RadioRepository {
        override fun observeStations(): Flow<List<RadioStation>> {
            error("Not used in this test")
        }

        override suspend fun getStations(): List<RadioStation> {
            return radioLocalDataSource.observeAll().first()
        }

        override suspend fun getById(id: String): RadioStation? {
            return radioLocalDataSource.getById(id)
        }

        override suspend fun upsert(station: RadioStation) {
            radioLocalDataSource.upsert(station)
        }

        override suspend fun delete(station: RadioStation) {
            radioLocalDataSource.delete(station)
        }
    }

    private companion object {
        const val TEST_DATABASE_NAME = "radio-station-management-test.db"
    }
}
