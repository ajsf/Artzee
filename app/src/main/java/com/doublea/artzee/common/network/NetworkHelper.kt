package com.doublea.artzee.common.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import io.reactivex.BackpressureStrategy
import io.reactivex.Emitter
import io.reactivex.Flowable

interface NetworkHelper {
    fun observeNetworkConnectivity(): Flowable<Boolean>
}

class NetworkHelperImpl(private val connectivityManager: ConnectivityManager) : NetworkHelper {

    private val networkStatusFlowable: Flowable<Boolean>

    private lateinit var statusEmitter: Emitter<Boolean>

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            statusEmitter.onNext(true)
        }

        override fun onLost(network: Network?) {
            statusEmitter.onNext(false)
        }
    }

    init {
        networkStatusFlowable = Flowable.create<Boolean>({ emitter ->
            statusEmitter = emitter

            emitter.setCancellable {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                val builder = NetworkRequest.Builder()
                connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
            }

            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            statusEmitter.onNext(activeNetwork?.isConnected == true)

        }, BackpressureStrategy.DROP)
    }

    override fun observeNetworkConnectivity(): Flowable<Boolean> = networkStatusFlowable
        .distinctUntilChanged()
}