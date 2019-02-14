package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.model.Art

class ArtEntityToArtMapper : Mapper<ArtEntity, Art> {
    override fun toModel(domain: ArtEntity) = with(domain) {
        Art(
                id, title, category, medium,
                date, collectingInstitution, image_versions, thumbnail,
                image, partner, genes, artists, similarArtworks)
    }

    override fun toDomain(model: Art) = with(model) {
        ArtEntity(
                id, title, category, medium,
                date, collectingInstitution, image_versions,
                thumbnail, image, partner, genes, artists, similarArtworks)
    }
}