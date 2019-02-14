package com.doublea.artzee.common.network

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.retrofit.ArtsyService
import io.reactivex.Single

data class ArtApiResponse(val artList: List<Art>, val cursor: String)

class ArtApi(private val service: ArtsyService,
             private val artMapper: Mapper<ArtsyArtworkWrapper, ArtApiResponse>,
             private val artistMapper: Mapper<ArtsyArtistsWrapper, Artist>
) {

    fun getArt(cursor: String = ""): Single<ArtApiResponse> = callArtsyService(cursor)
            .map { artMapper.toModel(it) }

    fun getArtistForArtwork(artworkId: String): Single<Artist> = service
            .getArtistsByArtworkId(artworkId)
            .map { artistMapper.toModel(it) }

    private fun callArtsyService(cursor: String): Single<ArtsyArtworkWrapper> {
        return if (cursor.isNotBlank()) service.getArtByCursor(cursor) else service.getArt()
    }

}