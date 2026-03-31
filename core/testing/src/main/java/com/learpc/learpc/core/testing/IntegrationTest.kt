package com.learpc.learpc.core.testing

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.learpc.learpc.core.database.AppDatabase
import org.junit.Rule
import org.junit.rules.ExternalResource

abstract class IntegrationTest {
    @get:Rule
    val databaseRule = InMemoryAppDatabaseRule()

    protected val database: AppDatabase
        get() = databaseRule.database
}

class InMemoryAppDatabaseRule : ExternalResource() {
    lateinit var database: AppDatabase
        private set

    override fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    override fun after() {
        if (::database.isInitialized) {
            database.close()
        }
    }
}
