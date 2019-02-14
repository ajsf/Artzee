package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtsyArtistResponse
import com.doublea.artzee.common.network.ArtsyArtistsWrapper

class ArtsyToArtistMapper : Mapper<ArtsyArtistsWrapper, Artist> {

    override fun toModel(domain: ArtsyArtistsWrapper): Artist = domain
            ._embedded.artists
            .map { it.toArtist() }
            .first()

    private fun ArtsyArtistResponse.toArtist() = Artist(
            this.id,
            this.name,
            this.birthday,
            this.deathday,
            this.hometown,
            this.nationality,
            this.image_versions,
            this._links?.thumbnail?.href,
            this._links?.image?.href,
            this._links?.artworks?.href,
            this._links?.similar_artists?.href,
            this._links?.similar_contemporary_artists?.href,
            this._links?.genes?.href
    )
}