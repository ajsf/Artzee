package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.common.network.ArtsyArtworkResponse

class ResponseToEntityMapper : Mapper<ArtApiResponse, List<ArtEntity>> {

    override fun toModel(domain: ArtApiResponse): List<ArtEntity> = domain.artList
        .map { it.toEntity() }

    private fun ArtsyArtworkResponse.toEntity() = ArtEntity(
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
        this._links?.similar_artworks?.href
    )
}