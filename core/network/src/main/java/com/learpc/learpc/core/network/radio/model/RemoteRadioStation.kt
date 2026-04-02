package com.learpc.learpc.core.network.radio.model

import org.json.JSONObject

data class RemoteRadioStation(
    val stationUuid: String,
    val name: String,
    val streamUrl: String,
    val resolvedStreamUrl: String? = null,
    val homepageUrl: String? = null,
    val artworkUrl: String? = null,
    val displayName: String? = null,
    val displayFrequency: String? = null,
    val frequency: String? = null,
    val band: String? = null,
    val sourceGroup: String? = null,
    val network: String? = null,
    val region: String? = null,
    val category: String? = null,
    val mediaType: String? = null,
    val uiPrimaryGroup: String? = null,
    val uiSecondaryGroup: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
    val language: String? = null,
    val codec: String? = null,
    val bitrateKbps: Int? = null,
    val tags: String? = null,
    val searchKeywords: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
    val mergedFromIds: List<String> = emptyList(),
    val favoriteDefault: Boolean = false
) {
    companion object {
        fun fromJson(json: JSONObject): RemoteRadioStation {
            return RemoteRadioStation(
                stationUuid = json.optString("stationuuid"),
                name = json.optString("name"),
                streamUrl = json.optString("url"),
                resolvedStreamUrl = json.optString("url_resolved").takeIf { it.isNotBlank() },
                homepageUrl = json.optString("homepage").takeIf { it.isNotBlank() },
                artworkUrl = json.optString("favicon").takeIf { it.isNotBlank() },
                country = json.optString("country").takeIf { it.isNotBlank() },
                countryCode = json.optString("countrycode").takeIf { it.isNotBlank() },
                language = json.optString("language").takeIf { it.isNotBlank() },
                codec = json.optString("codec").takeIf { it.isNotBlank() },
                bitrateKbps = json.optInt("bitrate").takeIf { it > 0 },
                tags = json.optString("tags").takeIf { it.isNotBlank() }
            )
        }
    }
}
