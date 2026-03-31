package com.learpc.learpc.core.media

import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.core.model.InterruptionSnapshot

object ResumeAfterInterruptionPolicy {
    const val MAX_INTERRUPTION_AGE_MS = 60 * 60 * 1000L

    fun shouldResume(snapshot: InterruptionSnapshot, settings: UserPreferences): Boolean {
        val interruptionAgeMs = System.currentTimeMillis() - snapshot.capturedAtEpochMs

        return settings.resumeAfterCallEnabled &&
            !snapshot.wasUserPause &&
            interruptionAgeMs in 0 until MAX_INTERRUPTION_AGE_MS
    }
}
