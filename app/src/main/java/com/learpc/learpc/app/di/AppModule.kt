package com.learpc.learpc.app.di

import com.learpc.learpc.app.system.ConnectivityManagerNetworkChecker
import com.learpc.learpc.core.common.NetworkChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindNetworkChecker(
        connectivityManagerNetworkChecker: ConnectivityManagerNetworkChecker
    ): NetworkChecker
}
