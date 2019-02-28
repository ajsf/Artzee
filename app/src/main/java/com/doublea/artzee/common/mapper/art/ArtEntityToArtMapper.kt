package com.doublea.artzee.common.mapper.art

import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art

class ArtEntityToArtMapper : Mapper<ArtEntity, Art> {
    override fun toModel(domain: ArtEntity) = with(domain) {
        Art(
            id, title, category, medium, date, collectingInstitution,
            thumbnail, image, imageRectangle, artistId
        )
    }

    override fun toDomain(model: Art): ArtEntity = with(model) {
        ArtEntity(
            id, title, category, medium, date, collectingInstitution,
            thumbnail, image, imageRectangle, artistId
        )
    }


}