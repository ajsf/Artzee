package com.doublea.artzee.common.mapper

import com.doublea.artzee.test.data.ArtDataFactory.randomApiResponse
import com.doublea.artzee.test.data.ArtsyApiDataFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtsyServiceToResponseMapperTest {

    private val nextUrl =
        "https://api.artsy.net/api/artworks?cursor=4eb1c7c5b4da19000100c841%3A4eb1c7c5b4da19000100c841&size=10"

    private val mapper = ArtsyServiceToResponseMapper()

    @Test
    fun `it extracts the cursor correctly`() {
        val wrapper = ArtsyApiDataFactory.randomArtworkWrapper(nextUrl)
        val response = mapper.toModel(wrapper)
        assertEquals("4eb1c7c5b4da19000100c841%3A4eb1c7c5b4da19000100c841", response.cursor)
    }

    @Test
    fun `it extracts the art correctly`() {
        val wrapper = ArtsyApiDataFactory.randomArtworkWrapper(nextUrl)
        val response = mapper.toModel(wrapper)
        val expectedList = wrapper._embedded.artworks
        assertEquals(expectedList, response.artList)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `toDomain throws an UnsupportedOperationException`() {
        val randomResponse = randomApiResponse()
        mapper.toDomain(randomResponse)
    }
}