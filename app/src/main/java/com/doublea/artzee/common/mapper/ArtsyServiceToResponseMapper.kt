package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.common.network.ArtsyArtworkResponse
import com.doublea.artzee.common.network.ArtsyArtworkWrapper

class ArtsyServiceToResponseMapper : Mapper<ArtsyArtworkWrapper, ArtApiResponse> {

    override fun toModel(domain: ArtsyArtworkWrapper): ArtApiResponse = domain
            .extractResponseData()

    private fun ArtsyArtworkWrapper.extractResponseData(): ArtApiResponse {
        val artList = _embedded.artworks.map { it.toArt() }
        val cursor = getCursor(_links.next.href)
        return ArtApiResponse(artList, cursor)
    }

    private fun ArtsyArtworkResponse.toArt() = Art(
            this.id,
            this.title,
            this.category,
            this.medium,
            this.date,
            this.collecting_institution,
            this.image_versions,
            this._links?.thumbnail?.href ?: "",
            this._links?.image?.href ?: "",
            this._links?.partner?.href,
            this._links?.genes?.href,
            this._links?.artists?.href,
            this._links?.similar_artworks?.href)

    private fun getCursor(urlString: String): String {
        val cursorIndex = urlString.indexOf("cursor=") + 7
        val endCursorIndex = urlString.indexOf("&size=")
        return urlString.slice(cursorIndex until endCursorIndex)
    }
}