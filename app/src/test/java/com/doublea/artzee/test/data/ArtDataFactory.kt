package com.doublea.artzee.test.data

import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.ArtApiResponse

object ArtDataFactory {

    fun randomApiResponse() = ArtApiResponse(
        TestDataFactory.randomList(ArtsyApiDataFactory::randomArtworkResponse),
        TestDataFactory.randomString()
    )

    fun randomArt() = Art(
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString(),
        TestDataFactory.randomString()
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
        TestDataFactory.randomString()
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
}