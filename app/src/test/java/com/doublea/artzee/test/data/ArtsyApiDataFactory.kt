package com.doublea.artzee.test.data

import com.doublea.artzee.common.network.*

object ArtsyApiDataFactory {

    fun randomArtworkWrapper() = ArtsyArtworkWrapper(
            randomLinksResponse(),
            randomEmbeddedArtworkResponse()
    )

    fun randomArtistsWrapper() = ArtsyArtistsWrapper(
            randomLinksResponse(),
            randomEmbeddedArtistsResponse()
    )

    private fun randomLinksResponse() = ArtsyLinksResponse(
            randomArtsyLink(), randomArtsyLink()
    )

    private fun randomArtsyLink() = ArtsyLink(TestDataFactory.randomString())

    private fun randomEmbeddedArtworkResponse() = ArtsyEmbeddedArtworkResponse(
            TestDataFactory.randomList(this::randomArtworkResponse)
    )

    private fun randomArtworkResponse() = ArtsyArtworkResponse(
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomStringList()
    )

    private fun randomEmbeddedArtistsResponse() = ArtsyEmbeddedArtistsResponse(
            TestDataFactory.randomList(this::randomArtistResponse)
    )

    private fun randomArtistResponse() = ArtsyArtistResponse(
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomString(),
            TestDataFactory.randomStringList()
    )
}