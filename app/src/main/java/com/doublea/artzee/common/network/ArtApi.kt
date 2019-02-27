package com.doublea.artzee.common.network

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.retrofit.ArtsyService
import io.reactivex.Maybe
import io.reactivex.Single

data class ArtApiResponse(val artList: List<ArtsyArtworkResponse>, val cursor: String)

class ArtApi(
    private val service: ArtsyService,
    private val artMapper: Mapper<ArtsyArtworkWrapper, ArtApiResponse>,
    private val artistMapper: Mapper<ArtsyArtistsWrapper, Artist>
) {

    fun getArtistForArtwork(artworkId: String): Maybe<Artist> = service
        .getArtistsByArtworkId(artworkId)
        .filter { it._embedded.artists.isNotEmpty() }
        .map { artistMapper.toModel(it) }

    fun getArt(cursor: String = ""): Single<ArtApiResponse> = callArtsyService(cursor)
        .map { artMapper.toModel(it) }

    private fun callArtsyService(cursor: String): Single<ArtsyArtworkWrapper> {
        return if (cursor.isNotBlank()) service.getArtByCursor(cursor) else service.getArt()
    }
}