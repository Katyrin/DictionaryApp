package com.katyrin.dictionaryapp.data.networkstatus

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

class NetworkStateImpl(context: Context) : NetworkState {

    private var isOnline: Boolean = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isOnline = true
        }

        override fun onUnavailable() {
            isOnline = false
        }

        override fun onLost(network: Network) {
            isOnline = false
        }
    }

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    override fun isOnline(): Boolean = isOnline
}