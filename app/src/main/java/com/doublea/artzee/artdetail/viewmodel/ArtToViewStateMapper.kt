package com.doublea.artzee.artdetail.viewmodel

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art

class ArtToViewStateMapper : Mapper<Art, ArtDetailViewState> {

    override fun toModel(domain: Art): ArtDetailViewState {
        val details = "${domain.medium}, ${domain.date}"
        val details2 = if (domain.collectingInstitution.isBlank()) "" else
            "Collecting institution: ${domain.collectingInstitution}"
        return ArtDetailViewState(domain.imageRectangle, domain.image, domain.title, details, details2)
    }
}