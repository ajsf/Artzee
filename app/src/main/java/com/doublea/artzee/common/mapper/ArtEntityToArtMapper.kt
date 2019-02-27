package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.model.Art

class ArtEntityToArtMapper : Mapper<ArtEntity, Art> {
    override fun toModel(domain: ArtEntity) = with(domain) {
        Art(
            id, title, category, medium,
            date, collectingInstitution, thumbnail,
            getImageUrl("larger"),
            getImageUrl("medium"),
            artistId
        )
    }

    private fun ArtEntity.getImageUrl(imageVersion: String) = this
        .image
        .replace("{image_version}", imageVersion)
}