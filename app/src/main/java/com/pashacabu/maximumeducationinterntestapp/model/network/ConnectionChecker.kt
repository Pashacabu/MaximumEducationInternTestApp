package com.pashacabu.maximumeducationinterntestapp.model.network

import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData
import java.nio.channels.NetworkChannel
import javax.inject.Inject

class ConnectionChecker @Inject constructor(context: Context) : LiveData<Boolean>() {

    private val manager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

    private var isRegistered = false

    override fun onActive() {
        super.onActive()
        if (!isRegistered){
            manager.registerNetworkCallback(
                networkRequest.build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        postValue(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        postValue(false)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        postValue(false)
                    }
                }
            )
        }
    }



}