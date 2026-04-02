package com.learpc.learpc.domain.usecase

internal object RadioPlaylistParser {
    fun parse(content: String, sourceUrl: String): String? {
        val trimmedSource = sourceUrl.trim()
        val lines = content.lineSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()

        return when {
            trimmedSource.endsWith(".pls", ignoreCase = true) -> parsePls(lines)
            trimmedSource.endsWith(".m3u", ignoreCase = true) -> parseM3u(lines)
            trimmedSource.endsWith(".xspf", ignoreCase = true) -> parseXspf(content)
            else -> parseM3u(lines) ?: parsePls(lines) ?: parseXspf(content)
        }
    }

    private fun parsePls(lines: List<String>): String? {
        return lines.asSequence()
            .firstOrNull { it.startsWith("File1=", ignoreCase = true) }
            ?.substringAfter('=')
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    private fun parseM3u(lines: List<String>): String? {
        return lines.asSequence()
            .firstOrNull { line ->
                !line.startsWith("#") &&
                    (line.startsWith("http://", ignoreCase = true) ||
                        line.startsWith("https://", ignoreCase = true))
            }
    }

    private fun parseXspf(content: String): String? {
        val regex = Regex("(?is)<location>\\s*(.*?)\\s*</location>")
        return regex.find(content)
            ?.groupValues
            ?.getOrNull(1)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }
}
