package com.learpc.learpc.core.network.di

import com.learpc.learpc.core.network.radio.RadioBrowserApiService
import com.learpc.learpc.core.network.rss.PodcastFeedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val RADIO_BROWSER_BASE_URL = "https://de1.api.radio-browser.info/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RADIO_BROWSER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePodcastFeedService(
        okHttpClient: OkHttpClient
    ): PodcastFeedService {
        return PodcastFeedService(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideRadioBrowserApiService(retrofit: Retrofit): RadioBrowserApiService {
        return retrofit.create(RadioBrowserApiService::class.java)
    }
}
