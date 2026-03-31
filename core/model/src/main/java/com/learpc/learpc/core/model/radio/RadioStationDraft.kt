package com.learpc.learpc.core.model.radio

data class RadioStationDraft(
    val name: String,
    val streamUrl: String,
    val resolvedStreamUrl: String? = null,
    val homepageUrl: String? = null,
    val artworkUrl: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
    val language: String? = null,
    val genre: String? = null,
    val codec: String? = null,
    val bitrateKbps: Int? = null
)
