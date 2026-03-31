package com.learpc.learpc.core.network.rss

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class PodcastFeedService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    suspend fun fetchFeed(url: String): Result<String> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val request = Request.Builder()
                    .url(url)
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected HTTP ${response.code}")
                    }

                    response.body?.string() ?: throw IOException("Empty RSS response body")
                }
            }
        }
    }
}
