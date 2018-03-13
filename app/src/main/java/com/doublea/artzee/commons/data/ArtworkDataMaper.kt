package com.doublea.artzee.commons.data

import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.data.network.ArtsyArtistResponse
import com.doublea.artzee.commons.data.network.ArtsyArtworkResponse

fun ArtsyArtworkResponse.toArt() = Art(
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

fun ArtsyArtistResponse.toArtist() = Artist(
        this.id,
        this.name,
        this.birthday,
        this.deathday,
        this.hometown,
        this.nationality,
        this.image_versions,
        this._links.thumbnail.href,
        this._links.image.href,
        this._links.artworks.href,
        this._links.similar_artists.href,
        this._links.similar_contemporary_artists.href,
        this._links.genes.href
)