package com.doublea.artzee.common.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.doublea.artzee.common.network.NetworkHelper
import com.doublea.artzee.common.network.NetworkHelperImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun appModule(appContext: Context) = Kodein.Module("appModule") {
    bind<Context>() with singleton { appContext }
    bind<SharedPreferences>() with singleton {
        appContext
            .getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)
    }
    bind<ConnectivityManager>() with singleton { appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    bind<NetworkHelper>() with singleton { NetworkHelperImpl(instance()) }
    import(dataModule())
}