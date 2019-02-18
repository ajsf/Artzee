package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.model.Art
import com.doublea.artzee.test.data.ArtDataFactory.randomApiResponse
import com.doublea.artzee.test.data.ArtsyApiDataFactory
import junit.framework.Assert.assertEquals
import org.junit.Test

class ArtsyServiceToResponseMapperTest {

    private val nextUrl = "https://api.artsy.net/api/artworks?cursor=4eb1c7c5b4da19000100c841%3A4eb1c7c5b4da19000100c841&size=10"

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
        val expectedList = wrapper._embedded.artworks.map {
            Art(it.id, it.title, it.category, it.medium, it.date,
                    it.collecting_institution, it.image_versions, it._links?.thumbnail?.href ?: "",
                    it._links?.image?.href ?: "", it._links?.partner?.href, it._links?.genes?.href,
                    it._links?.artists?.href, it._links?.similar_artworks?.href)
        }
        assertEquals(expectedList, response.artList)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `toDomain throws an UnsupportedOperationException`() {
        val randomResponse = randomApiResponse()
        mapper.toDomain(randomResponse)
    }
}