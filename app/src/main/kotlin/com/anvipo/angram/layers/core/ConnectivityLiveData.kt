package com.anvipo.angram.layers.core

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData

/**
 * A LiveData class which wraps the network connection status
 * Requires Permission: ACCESS_NETWORK_STATE
 *
 * See https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
 * See https://developer.android.com/reference/android/net/ConnectivityManager
 * See https://developer.android.com/reference/android/net/ConnectivityManager#CONNECTIVITY_ACTION
 */
@Suppress("unused")
class ConnectivityLiveData(
    private val connectivityManager: ConnectivityManager
) : LiveData<NetworkConnectionState>() {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    constructor(application: Application) : this(
        application.getSystemService<ConnectivityManager>()!!
    )

    override fun onActive() {
        super.onActive()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            super.onAvailable(network)
            val connectionState: Int? = if (network != null) {
                val networkCapabilities: NetworkCapabilities? =
                    connectivityManager.getNetworkCapabilities(network)

                when {
                    networkCapabilities == null -> null
                    networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> TRANSPORT_CELLULAR
                    networkCapabilities.hasTransport(TRANSPORT_WIFI) -> TRANSPORT_WIFI
                    networkCapabilities.hasTransport(TRANSPORT_BLUETOOTH) -> TRANSPORT_BLUETOOTH
                    networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> TRANSPORT_ETHERNET
                    networkCapabilities.hasTransport(TRANSPORT_VPN) -> TRANSPORT_VPN
                    networkCapabilities.hasTransport(TRANSPORT_WIFI_AWARE) ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            TRANSPORT_WIFI_AWARE
                        } else {
                            null
                        }
                    networkCapabilities.hasTransport(TRANSPORT_LOWPAN) ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                            TRANSPORT_LOWPAN
                        } else {
                            null
                        }
                    else -> null
                }
            } else {
                null
            }

            postValue(
                NetworkConnectionState(
                    isAvailable = true,
                    connectionState = connectionState
                )
            )
        }

        override fun onUnavailable() {
            postValue(
                NetworkConnectionState(
                    isAvailable = false
                )
            )
        }

        override fun onLost(network: Network?) {
            postValue(
                NetworkConnectionState(
                    isAvailable = false
                )
            )
        }

    }

}