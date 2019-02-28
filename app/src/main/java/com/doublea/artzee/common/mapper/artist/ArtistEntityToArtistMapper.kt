package com.doublea.artzee.common.mapper.artist

import com.doublea.artzee.common.db.room.ArtistEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Artist

class ArtistEntityToArtistMapper : Mapper<ArtistEntity, Artist> {
    override fun toModel(domain: ArtistEntity) = with(domain) {
        Artist(
            id, name, birthday, deathday,
            hometown, nationality, image_versions,
            thumbnail, image, artworks,
            similar_artists, similar_contemporary_artists, genes
        )
    }

    override fun toDomain(model: Artist): ArtistEntity = with(model) {
        ArtistEntity(
            id, name, birthday, deathday,
            hometown, nationality, image_versions,
            thumbnail, image, artworks,
            similar_artists, similar_contemporary_artists, genes
        )
    }
}