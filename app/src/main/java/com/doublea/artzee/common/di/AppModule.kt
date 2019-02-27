package com.doublea.artzee.common.di

import android.content.Context
import android.content.SharedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun appModule(appContext: Context) = Kodein.Module("appModule") {
    bind<Context>() with provider { appContext }
    bind<SharedPreferences>() with singleton {
        appContext
            .getSharedPreferences(appContext.packageName, Context.MODE_PRIVATE)
    }
    import(dataModule())
}