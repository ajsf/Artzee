package com.doublea.artzee.common.mapper.art

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtsyArtworkResponse

class ArtworkResponseToArtMapper : Mapper<ArtsyArtworkResponse, Art> {
    override fun toModel(domain: ArtsyArtworkResponse) = with(domain) {

        val thumbnail = _links?.thumbnail?.href ?: ""
        val baseImageUrl = _links?.image?.href ?: ""

        val largerImage = getImageUrl(baseImageUrl, "larger")

        val mediumImage = getImageUrl(baseImageUrl, "medium")

        Art(
            id, title, category, medium,
            date, collecting_institution, thumbnail,
            largerImage, mediumImage, null
        )
    }

    private fun getImageUrl(baseUrl: String, imageVersion: String): String =
        if (baseUrl.isNotEmpty()) baseUrl.replace("{image_version}", imageVersion) else ""
}