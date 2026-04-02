package com.learpc.learpc.core.network.radio

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RadioBrowserApiService {
    @GET("json/stations/topclick/{limit}")
    suspend fun getTopStations(@Path("limit") limit: Int): String

    @GET("json/stations/search")
    suspend fun searchStations(
        @Query("name") name: String,
        @Query("hidebroken") hideBroken: Boolean = true,
        @Query("limit") limit: Int = 25
    ): String
}
