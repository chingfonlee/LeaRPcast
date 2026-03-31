package com.learpc.learpc.core.model

sealed class PlaybackError {
    data object Unknown : PlaybackError()
    data object Network : PlaybackError()
    data object SourceUnavailable : PlaybackError()
    data object UnsupportedFormat : PlaybackError()
}
