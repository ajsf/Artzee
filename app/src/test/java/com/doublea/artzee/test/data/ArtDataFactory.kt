package com.doublea.artzee.test.data

import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewState
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.db.room.ArtistEntity
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtApiResponse

object ArtDataFactory {

    fun randomApiResponse() = ArtApiResponse(
        TestDataFactory.randomList(ArtsyApiDataFactory::randomArtworkResponse),
        TestDataFactory.randomString()
    )

    fun randomArt(artistId: String? = TestDataFactory.randomString()) = Art(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        artistId
    )

    fun randomArtEntity() = ArtEntity(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomStringList(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        null
    )

    fun randomArtist() = Artist(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomStringList(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString()
    )

    fun randomArtistEntity() = ArtistEntity(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomStringList(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString()
    )

    fun randomViewState() = ArtDetailViewState(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString()
    )
}