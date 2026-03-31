package com.learpc.learpc.app.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.learpc.learpc.core.common.NetworkChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectivityManagerNetworkChecker @Inject constructor(
    @ApplicationContext context: Context
) : NetworkChecker {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isOnUnmeteredNetwork(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork
        ) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
    }
}
