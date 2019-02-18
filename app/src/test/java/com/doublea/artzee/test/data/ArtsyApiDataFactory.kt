package com.doublea.artzee.test.data

import com.doublea.artzee.common.network.*

object ArtsyApiDataFactory {

    fun randomArtworkWrapper(nextLink: String = TestDataFactory.randomString()) = ArtsyArtworkWrapper(
            randomLinksResponse(nextLink),
            randomEmbeddedArtworkResponse()
    )

    fun randomArtistsWrapper(nextLink: String = TestDataFactory.randomString()) = ArtsyArtistsWrapper(
            randomLinksResponse(nextLink),
            randomEmbeddedArtistsResponse()
    )

    private fun randomLinksResponse(nextLink: String) = ArtsyLinksResponse(
            randomArtsyLink(), ArtsyLink(nextLink)
    )

    private fun randomArtsyLink() = ArtsyLink(TestDataFactory.randomString())

    private fun randomEmbeddedArtworkResponse() = ArtsyEmbeddedArtworkResponse(
            TestDataFactory.randomList(this::randomArtworkResponse)
    )

    fun randomArtworkResponse() = ArtsyArtworkResponse(
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