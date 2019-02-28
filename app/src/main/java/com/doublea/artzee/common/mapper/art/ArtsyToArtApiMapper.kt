package com.doublea.artzee.common.mapper.art

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.common.network.ArtsyArtworkResponse
import com.doublea.artzee.common.network.ArtsyArtworkWrapper

class ArtsyToArtApiMapper(private val responseMapper: Mapper<ArtsyArtworkResponse, Art>) :
    Mapper<ArtsyArtworkWrapper, ArtApiResponse> {

    override fun toModel(domain: ArtsyArtworkWrapper): ArtApiResponse = domain
        .extractResponseData()

    private fun ArtsyArtworkWrapper.extractResponseData(): ArtApiResponse {
        val artList = _embedded.artworks.map { responseMapper.toModel(it) }
        val cursor = getCursor(_links.next.href)
        return ArtApiResponse(artList, cursor)
    }

    private fun getCursor(urlString: String): String {
        val cursorIndex = urlString.indexOf("cursor=") + 7
        val endCursorIndex = urlString.indexOf("&size=")
        return urlString.slice(cursorIndex until endCursorIndex)
    }
}