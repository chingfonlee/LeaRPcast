package com.learpc.learpc.feature.podcast.navigation

const val PODCAST_ROUTE = "podcast"
const val PODCAST_DETAIL_ROUTE = "podcast_detail"
const val PODCAST_EPISODES_ROUTE = "podcast_episodes"
const val PODCAST_ID_ARG = "podcastId"

fun podcastDetailRoute(podcastId: String): String = "$PODCAST_DETAIL_ROUTE/$podcastId"
fun podcastEpisodesRoute(podcastId: String): String = "$PODCAST_EPISODES_ROUTE/$podcastId"
