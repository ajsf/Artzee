package com.doublea.artzee.common.network

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.retrofit.ArtsyService
import io.reactivex.Flowable
import io.reactivex.Single

data class ArtApiResponse(val artList: List<Art>? = null, val cursor: String? = null, val error: String? = null)

data class ArtistApiResponse(val artist: Artist? = null, val error: String? = null)

class ArtApi(
    private val service: ArtsyService,
    private val artMapper: Mapper<ArtsyArtworkWrapper, ArtApiResponse>,
    private val artistMapper: Mapper<ArtsyArtistsWrapper, ArtistApiResponse>,
    private val networkHelper: NetworkHelper
) {

    private val errorMessage = "No Internet Connection"

    fun getArtistForArtwork(artworkId: String): Flowable<ArtistApiResponse> {
        val errorFlowable = errorFlowable()
            .map { ArtistApiResponse(error = errorMessage) }

        val successFlowable = successFlowable()
            .flatMapSingle { service.getArtistsByArtworkId(artworkId) }
            .filter { it._embedded.artists.isNotEmpty() }
            .map { artistMapper.toModel(it) }

        return errorFlowable.mergeWith(successFlowable)
    }

    fun getArt(cursor: String = ""): Flowable<ArtApiResponse> {
        val errorFlowable = errorFlowable()
            .map { ArtApiResponse(error = errorMessage) }

        val successFlowable = successFlowable()
            .flatMapSingle { callArtsyService(cursor) }
            .map { artMapper.toModel(it) }

        return errorFlowable.mergeWith(successFlowable)
    }

    private fun errorFlowable(): Flowable<Boolean> = getNetworkConnectivity()
        .filter { !it }

    private fun successFlowable(): Flowable<Boolean> = getNetworkConnectivity()
        .filter { it }

    private fun getNetworkConnectivity() = networkHelper.observeNetworkConnectivity()

    private fun callArtsyService(cursor: String): Single<ArtsyArtworkWrapper> {
        return if (cursor.isNotBlank()) service.getArtByCursor(cursor) else service.getArt()
    }
}