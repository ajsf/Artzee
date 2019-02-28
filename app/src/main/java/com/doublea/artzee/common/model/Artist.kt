package com.doublea.artzee.common.model

data class Artist(
        val id: String,
        val name: String,
        val birthday: String,
        val deathday: String,
        val hometown: String?,
        val nationality: String,
        val image_versions: List<String>?,
        val thumbnail: String?,
        val image: String?,
        val artworks: String?,
        val similar_artists: String?,
        val similar_contemporary_artists: String?,
        val genes: String?
)