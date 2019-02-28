package com.doublea.artzee.common.di

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.mapper.art.ArtsyToArtApiMapper
import com.doublea.artzee.common.mapper.art.ArtworkResponseToArtMapper
import com.doublea.artzee.common.mapper.artist.ArtsyToArtistApiMapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.*
import com.doublea.artzee.common.network.retrofit.ArtsyService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun networkModule() = Kodein.Module("networkModule") {
    bind<ArtsyService>() with singleton { ArtsyService.getService() }
    bind<ArtApi>() with singleton { ArtApi(instance(), instance(), instance(), instance()) }
    bind<Mapper<ArtsyArtworkResponse, Art>>() with provider { ArtworkResponseToArtMapper() }
    bind<Mapper<ArtsyArtworkWrapper, ArtApiResponse>>() with provider {
        ArtsyToArtApiMapper(
            instance()
        )
    }
    bind<Mapper<ArtsyArtistsWrapper, ArtistApiResponse>>() with provider { ArtsyToArtistApiMapper() }
}