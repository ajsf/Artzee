package com.doublea.artzee.common.di

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtDatabase
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.ArtEntityToArtMapper
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun dbModule() = Kodein.Module("dbModule") {
    bind<Mapper<ArtEntity, Art>>() with provider { ArtEntityToArtMapper() }
    bind<ArtsyCache>() with singleton { ArtsyCache(instance(), instance()) }
    bind<ArtDao>() with provider { ArtDatabase.getInstance(instance()).artDao() }
}