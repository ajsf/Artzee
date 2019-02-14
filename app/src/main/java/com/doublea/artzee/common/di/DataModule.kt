package com.doublea.artzee.common.di

import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.data.ArtRepositoryImpl
import com.doublea.artzee.common.data.PreferencesHelper
import com.doublea.artzee.common.data.SharedPrefsHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun dataModule() = Kodein.Module("dataModule") {
    import(dbModule())
    import(networkModule())
    bind<ArtRepository>() with singleton { ArtRepositoryImpl(instance(), instance(), instance()) }
    bind<PreferencesHelper>() with provider { SharedPrefsHelper(instance()) }
}