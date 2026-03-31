package com.learpc.learpc.core.media.factory

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import javax.inject.Inject

class DefaultMediaSessionFactory @Inject constructor() : MediaSessionFactory {
    override fun create(context: Context, player: Player): MediaSession {
        return MediaSession.Builder(context, player).build()
    }
}
