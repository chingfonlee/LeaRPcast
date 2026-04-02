package com.learpc.learpc.app.data.source

import android.content.Context
import com.learpc.learpc.core.model.radio.RadioStation
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.json.JSONObject

private const val CATALOG_ASSET_PATH = "radio/taiwan_radio_normalized_deduped.json"

class TaiwanRadioCatalogSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun loadStations(
        existingStations: Map<String, RadioStation>,
        now: Long
    ): Result<List<RadioStation>> {
        return runCatching {
            val payload = context.assets.open(CATALOG_ASSET_PATH)
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            val jsonArray = JSONObject(payload).getJSONArray("stations")
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    val station = item.toCatalogStation(
                        now = now,
                        existing = existingStations[item.optString("id")],
                        fallbackSortOrder = index
                    )
                    if (station.id.isNotBlank() && station.name.isNotBlank() && station.streamUrl.isNotBlank()) {
                        add(station)
                    }
                }
            }
        }
    }

    private fun JSONObject.toCatalogStation(
        now: Long,
        existing: RadioStation?,
        fallbackSortOrder: Int
    ): RadioStation {
        val searchKeywords = optStringArray("search_keywords")
        val aliases = optStringArray("aliases")
        val mergedFromIds = optStringArray("merged_from_ids")
        val favoriteDefault = optBoolean("favorite_default", false)
        val displayName = optString("display_name").takeIf { it.isNotBlank() }
        val displayFrequency = optString("display_frequency").takeIf { it.isNotBlank() }
        val region = optString("region").takeIf { it.isNotBlank() }
        val network = optString("network").takeIf { it.isNotBlank() }
        val uiPrimaryGroup = optString("ui_primary_group").takeIf { it.isNotBlank() }
        val uiSecondaryGroup = optString("ui_secondary_group").takeIf { it.isNotBlank() }
        val sourceGroup = optString("source_group").takeIf { it.isNotBlank() }
        val category = optString("category").takeIf { it.isNotBlank() }
        val mediaType = optString("media_type").takeIf { it.isNotBlank() }
        val band = optString("band").takeIf { it.isNotBlank() }
        val frequency = optString("frequency").takeIf { it.isNotBlank() }

        return RadioStation(
            id = optString("id"),
            name = optString("name"),
            streamUrl = optString("stream_url"),
            resolvedStreamUrl = optString("resolved_stream_url").takeIf { it.isNotBlank() },
            homepageUrl = optString("homepage_url").takeIf { it.isNotBlank() },
            artworkUrl = optString("artwork_url").takeIf { it.isNotBlank() },
            displayName = displayName,
            displayFrequency = displayFrequency,
            frequency = frequency,
            band = band,
            sourceGroup = sourceGroup,
            network = network,
            region = region,
            category = category,
            mediaType = mediaType,
            uiPrimaryGroup = uiPrimaryGroup,
            uiSecondaryGroup = uiSecondaryGroup,
            country = optString("country").takeIf { it.isNotBlank() } ?: "Taiwan",
            countryCode = optString("country_code").takeIf { it.isNotBlank() } ?: "TW",
            language = optString("language").takeIf { it.isNotBlank() },
            genre = category ?: uiPrimaryGroup ?: uiSecondaryGroup,
            codec = optString("codec").takeIf { it.isNotBlank() },
            bitrateKbps = optInt("bitrate_kbps").takeIf { it > 0 },
            searchKeywords = searchKeywords,
            aliases = aliases,
            mergedFromIds = mergedFromIds,
            isFavorite = existing?.isFavorite ?: favoriteDefault,
            sortOrder = existing?.sortOrder ?: if (has("sort_order")) {
                optInt("sort_order").takeIf { it >= 0 }
            } else {
                null
            } ?: fallbackSortOrder,
            lastSyncedAt = now,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )
    }

    private fun JSONObject.optStringArray(name: String): List<String> {
        val jsonArray = optJSONArray(name) ?: return emptyList()
        return buildList {
            for (index in 0 until jsonArray.length()) {
                val value = jsonArray.optString(index).trim()
                if (value.isNotBlank()) {
                    add(value)
                }
            }
        }
    }

    private fun JSONObject.optBoolean(name: String, fallback: Boolean): Boolean {
        return if (has(name) && !isNull(name)) optBoolean(name) else fallback
    }
}
