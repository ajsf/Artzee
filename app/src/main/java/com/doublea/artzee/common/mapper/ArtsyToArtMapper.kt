package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtsyArtworkResponse
import com.doublea.artzee.common.network.ArtsyArtworkWrapper

class ArtsyToArtMapper : Mapper<ArtsyArtworkWrapper, List<Art>> {

    override fun toModel(domain: ArtsyArtworkWrapper): List<Art> = domain
            ._embedded.artworks.map { it.toArt() }

    private fun ArtsyArtworkResponse.toArt() = Art(
            this.id,
            this.title,
            this.category,
            this.medium,
            this.date,
            this.collecting_institution,
            this.image_versions,
            this._links.thumbnail.href,
            this._links.image.href,
            this._links.partner.href,
            this._links.genes.href,
            this._links.artists.href,
            this._links.similar_artworks.href)
}