package com.learpc.learpc.domain.usecase

import com.learpc.learpc.core.model.radio.RadioStation
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ResolvedRadioStream(
    val mediaUri: String,
    val fallbackMediaUri: String? = null
)

class ResolveRadioStreamUseCase @Inject constructor() {
    suspend operator fun invoke(station: RadioStation): ResolvedRadioStream = withContext(Dispatchers.IO) {
        val primaryCandidate = station.resolvedStreamUrl
            ?.trim()
            ?.takeIf { it.isNotBlank() && it != station.streamUrl.trim() }
            ?: station.streamUrl.trim()

        val fallbackCandidate = when {
            primaryCandidate == station.resolvedStreamUrl?.trim() ->
                station.streamUrl.trim().takeIf { it.isNotBlank() && it != primaryCandidate }
            primaryCandidate == station.streamUrl.trim() ->
                station.resolvedStreamUrl?.trim()?.takeIf { it.isNotBlank() && it != primaryCandidate }
            else -> null
        }

        val resolvedPrimary = resolvePlayableCandidate(primaryCandidate)
        val resolvedFallback = fallbackCandidate?.let { resolvePlayableCandidate(it) }

        when {
            resolvedPrimary != null -> ResolvedRadioStream(
                mediaUri = resolvedPrimary,
                fallbackMediaUri = resolvedFallback ?: fallbackCandidate
            )
            resolvedFallback != null -> ResolvedRadioStream(
                mediaUri = resolvedFallback,
                fallbackMediaUri = primaryCandidate.takeIf { it.isNotBlank() && it != resolvedFallback }
            )
            else -> ResolvedRadioStream(
                mediaUri = primaryCandidate,
                fallbackMediaUri = fallbackCandidate
            )
        }
    }

    private fun resolvePlayableCandidate(candidate: String): String? {
        return when {
            candidate.isPlaylistUrl() -> resolvePlaylist(candidate)
            else -> candidate
        }
    }

    private fun resolvePlaylist(url: String): String? {
        return runCatching {
            val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5_000
                readTimeout = 5_000
                instanceFollowRedirects = true
                requestMethod = "GET"
            }

            try {
                connection.inputStream.bufferedReader().use { reader ->
                    val content = reader.readText()
                    RadioPlaylistParser.parse(content = content, sourceUrl = url)
                }
            } finally {
                connection.disconnect()
            }
        }.getOrNull()
    }

    private fun String.isPlaylistUrl(): Boolean {
        val normalized = trim().lowercase()
        return normalized.endsWith(".pls") ||
            normalized.endsWith(".m3u") ||
            normalized.endsWith(".xspf")
    }
}
