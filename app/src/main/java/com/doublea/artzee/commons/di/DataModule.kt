package com.doublea.artzee.commons.di

import com.doublea.artzee.commons.repository.ArtRepository
import com.doublea.artzee.commons.data.PreferencesHelper
import com.doublea.artzee.commons.data.SharedPrefsHelper
import com.doublea.artzee.commons.db.ArtsyCache
import com.doublea.artzee.commons.db.room.ArtDao
import com.doublea.artzee.commons.db.room.ArtDatabase
import com.doublea.artzee.commons.data.network.ArtsyService
import com.doublea.artzee.commons.repository.ArtRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.concurrent.Executors

fun dataModule() = Kodein.Module("dataModule") {
    bind<ArtRepository>() with singleton { ArtRepositoryImpl(instance(), instance(), instance()) }
    bind<ArtsyService>() with singleton { ArtsyService.getService() }
    bind<ArtsyCache>() with singleton { ArtsyCache(instance(), Executors.newSingleThreadExecutor()) }
    bind<ArtDao>() with provider { ArtDatabase.getInstance(instance()).artDao() }
    bind<PreferencesHelper>() with provider { SharedPrefsHelper(instance()) }
}