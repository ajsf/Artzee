package com.doublea.artzee.commons.data.network

data class ArtsyToken(val type: String, val token: String, val expires_at: String)

data class ArtsyArtworkWrapper(val _links: ArtsyLinksResponse,
                               val _embedded: ArtsyEmbeddedArtworkResponse)

data class ArtsyArtistsWrapper(val _links: ArtsyLinksResponse,
                               val _embedded: ArtsyEmbeddedArtistsResponse)

data class ArtsyLinksResponse(val self: ArtsyLink, val next: ArtsyLink)

data class ArtsyLink(val href: String)

data class ArtsyEmbeddedArtworkResponse(val artworks: List<ArtsyArtworkResponse>)

data class ArtsyEmbeddedArtistsResponse(val artists: List<ArtsyArtistResponse>)

data class ArtsyArtworkResponse(
        val id: String,
        val title: String,
        val category: String,
        val medium: String,
        val date: String,
        val collecting_institution: String,
        val image_versions: List<String>,
        val _links: ArtsyArtworkLinks
)

data class ArtsyArtworkLinks(
        val thumbnail: ArtsyLink,
        val image: ArtsyLink,
        val partner: ArtsyLink,
        val genes: ArtsyLink,
        val artists: ArtsyLink,
        val similar_artworks: ArtsyLink
)

data class ArtsyArtistResponse(
        val id: String,
        val name: String,
        val birthday: String,
        val deathday: String,
        val hometown: String,
        val nationality: String,
        val image_versions: List<String>,
        val _links: ArtsyArtistLinks
)

data class ArtsyArtistLinks(
        val thumbnail: ArtsyLink,
        val image: ArtsyLink,
        val artworks: ArtsyLink,
        val similar_artists: ArtsyLink,
        val similar_contemporary_artists: ArtsyLink,
        val genes: ArtsyLink
)
