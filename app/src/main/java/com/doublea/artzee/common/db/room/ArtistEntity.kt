package com.doublea.artzee.common.db.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val birthday: String,
    val deathday: String,
    val hometown: String,
    val nationality: String,
    val image_versions: List<String>?,
    val thumbnail: String?,
    val image: String?,
    val artworks: String?,
    val similar_artists: String?,
    val similar_contemporary_artists: String?,
    val genes: String?
)