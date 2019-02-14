package com.doublea.artzee.common.di

import com.doublea.artzee.common.mapper.ArtsyToArtMapper
import com.doublea.artzee.common.mapper.ArtsyToArtistMapper
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtsyArtistsWrapper
import com.doublea.artzee.common.network.ArtsyArtworkWrapper
import com.doublea.artzee.common.network.retrofit.ArtsyService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun networkModule() = Kodein.Module("networkModule") {
    bind<ArtsyService>() with singleton { ArtsyService.getService() }
    bind<ArtApi>() with provider { ArtApi(instance(), instance(), instance()) }
    bind<Mapper<ArtsyArtworkWrapper, List<Art>>>() with provider { ArtsyToArtMapper() }
    bind<Mapper<ArtsyArtistsWrapper, Artist>>() with provider { ArtsyToArtistMapper() }
}