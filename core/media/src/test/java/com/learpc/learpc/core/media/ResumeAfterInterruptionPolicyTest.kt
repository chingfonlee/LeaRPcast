package com.learpc.learpc.core.media

import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.model.InterruptionSnapshot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumeAfterInterruptionPolicyTest {
    @Test
    fun `returns false when resume after call is disabled`() {
        val snapshot = sampleSnapshot(ageMs = 10 * 60 * 1000L)

        assertFalse(
            ResumeAfterInterruptionPolicy.shouldResume(
                snapshot = snapshot,
                settings = samplePreferences(resumeAfterCallEnabled = false)
            )
        )
    }

    @Test
    fun `returns false when snapshot was caused by user pause`() {
        val snapshot = sampleSnapshot(
            ageMs = 10 * 60 * 1000L,
            wasUserPause = true
        )

        assertFalse(
            ResumeAfterInterruptionPolicy.shouldResume(
                snapshot = snapshot,
                settings = samplePreferences()
            )
        )
    }

    @Test
    fun `returns false when snapshot is older than threshold`() {
        val snapshot = sampleSnapshot(ageMs = ResumeAfterInterruptionPolicy.MAX_INTERRUPTION_AGE_MS + 60_000L)

        assertFalse(
            ResumeAfterInterruptionPolicy.shouldResume(
                snapshot = snapshot,
                settings = samplePreferences()
            )
        )
    }

    @Test
    fun `returns true when all conditions are met`() {
        val snapshot = sampleSnapshot(ageMs = 5 * 60 * 1000L)

        assertTrue(
            ResumeAfterInterruptionPolicy.shouldResume(
                snapshot = snapshot,
                settings = samplePreferences()
            )
        )
    }

    private fun sampleSnapshot(
        ageMs: Long,
        wasUserPause: Boolean = false
    ): InterruptionSnapshot {
        return InterruptionSnapshot(
            wasUserPause = wasUserPause,
            capturedAtEpochMs = System.currentTimeMillis() - ageMs
        )
    }

    private fun samplePreferences(
        resumeAfterCallEnabled: Boolean = true
    ): UserPreferences {
        return UserPreferences(
            resumeAfterCallEnabled = resumeAfterCallEnabled
        )
    }
}
