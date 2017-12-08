package com.doublea.artzee.commons.data.network

data class ArtsyToken(val type: String, val token: String, val expires_at: String)

data class ArtsyResponse(val _links: ArtsyLinksResponse, val _embedded: ArtsyEmbeddedResponse)

data class ArtsyLinksResponse(val self: ArtsyLink, val next: ArtsyLink)

data class ArtsyLink(val href: String)

data class ArtsyEmbeddedResponse(val artworks: List<ArtsyArtworkResponse>)

data class ArtsyArtworkResponse(
        val id: String,
        val title: String,
        val category: String,
        val medium: String,
        val date: String,
        val collecting_institution: String,
        val image_versions: List<String>)