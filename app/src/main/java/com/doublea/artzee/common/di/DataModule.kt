package com.doublea.artzee.common.di

import com.doublea.artzee.common.data.*
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun dataModule() = Kodein.Module("dataModule") {
    import(dbModule())
    import(networkModule())
    bind<Scheduler>() with provider { Schedulers.io() }
    bind<ArtRepository>() with singleton { ArtRepositoryImpl(instance(), instance(), instance()) }
    bind<PreferencesHelper>() with provider { SharedPrefsHelper(instance()) }
    bind<ArtPagedListBuilder>() with provider { ArtPagedListBuilder(instance(), instance()) }
    bind<ArtBoundaryCallback>() with provider {
        ArtBoundaryCallback(
            instance(),
            instance(),
            instance()
        )
    }
}