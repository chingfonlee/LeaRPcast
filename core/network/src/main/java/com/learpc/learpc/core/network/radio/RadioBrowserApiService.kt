package com.learpc.learpc.core.network.radio

import retrofit2.http.GET
import retrofit2.http.Path

interface RadioBrowserApiService {
    @GET("json/stations/topclick/{limit}")
    suspend fun getTopStations(@Path("limit") limit: Int): String
}
