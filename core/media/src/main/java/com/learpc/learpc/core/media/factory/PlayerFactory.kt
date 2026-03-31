package com.learpc.learpc.core.media.factory

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer

interface PlayerFactory {
    fun create(context: Context): ExoPlayer
}
