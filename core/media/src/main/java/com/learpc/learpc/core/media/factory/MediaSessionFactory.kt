package com.learpc.learpc.core.media.factory

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaSession

interface MediaSessionFactory {
    fun create(context: Context, player: Player): MediaSession
}
