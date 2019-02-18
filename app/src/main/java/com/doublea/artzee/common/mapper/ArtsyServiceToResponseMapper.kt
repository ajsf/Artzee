package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.common.network.ArtsyArtworkWrapper

class ArtsyServiceToResponseMapper : Mapper<ArtsyArtworkWrapper, ArtApiResponse> {

    override fun toModel(domain: ArtsyArtworkWrapper): ArtApiResponse = domain
        .extractResponseData()

    private fun ArtsyArtworkWrapper.extractResponseData(): ArtApiResponse {
        val artList = _embedded.artworks
        val cursor = getCursor(_links.next.href)
        return ArtApiResponse(artList, cursor)
    }

    private fun getCursor(urlString: String): String {
        val cursorIndex = urlString.indexOf("cursor=") + 7
        val endCursorIndex = urlString.indexOf("&size=")
        return urlString.slice(cursorIndex until endCursorIndex)
    }
}