package com.doublea.artzee.common.di

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.db.room.*
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.mapper.art.ArtEntityToArtMapper
import com.doublea.artzee.common.mapper.artist.ArtistEntityToArtistMapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun dbModule() = Kodein.Module("dbModule") {
    bind<ArtsyCache>() with singleton {
        ArtsyCache(
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
    bind<ArtDao>() with provider { ArtDatabase.getInstance(instance()).artDao() }
    bind<ArtistDao>() with singleton { ArtDatabase.getInstance(instance()).artistDao() }
    bind<Mapper<ArtEntity, Art>>() with provider { ArtEntityToArtMapper() }
    bind<Mapper<ArtistEntity, Artist>>() with provider { ArtistEntityToArtistMapper() }
}