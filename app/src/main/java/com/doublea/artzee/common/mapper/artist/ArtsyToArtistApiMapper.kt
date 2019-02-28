package com.doublea.artzee.common.mapper.artist

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtistApiResponse
import com.doublea.artzee.common.network.ArtsyArtistResponse
import com.doublea.artzee.common.network.ArtsyArtistsWrapper

class ArtsyToArtistApiMapper : Mapper<ArtsyArtistsWrapper, ArtistApiResponse> {

    override fun toModel(domain: ArtsyArtistsWrapper): ArtistApiResponse = domain
        ._embedded.artists
        .map { ArtistApiResponse(it.toArtist()) }
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