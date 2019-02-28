package com.doublea.artzee.common.mapper

import com.doublea.artzee.common.mapper.art.ArtsyToArtApiMapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.network.ArtsyArtworkResponse
import com.doublea.artzee.test.data.ArtDataFactory.randomApiResponse
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.doublea.artzee.test.data.ArtsyApiDataFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArtsyToArtApiMapperTest {

    private val nextUrl =
        "https://api.artsy.net/api/artworks?cursor=4eb1c7c5b4da19000100c841%3A4eb1c7c5b4da19000100c841&size=10"

    @Mock
    lateinit var mockMapper: Mapper<ArtsyArtworkResponse, Art>

    private lateinit var mapper: ArtsyToArtApiMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mapper = ArtsyToArtApiMapper(mockMapper)
    }

    @Test
    fun `it extracts the cursor correctly`() {
        val wrapper = ArtsyApiDataFactory.randomArtworkWrapper(nextUrl)
        val response = mapper.toModel(wrapper)
        assertEquals("4eb1c7c5b4da19000100c841%3A4eb1c7c5b4da19000100c841", response.cursor)
    }

    @Test
    fun `it calls toModel on the mapper for each artwork`() {
        val wrapper = ArtsyApiDataFactory.randomArtworkWrapper(nextUrl)
        val artworkList = wrapper._embedded.artworks
        stubMapper(artworkList)

        mapper.toModel(wrapper)

        artworkList.onEach {
            verify(mockMapper).toModel(it)
        }
    }

    @Test
    fun `it returns the mapped art`() {
        val wrapper = ArtsyApiDataFactory.randomArtworkWrapper(nextUrl)
        val artworkList = wrapper._embedded.artworks
        val mappedList = stubMapper(artworkList)

        val response = mapper.toModel(wrapper)

        assertEquals(mappedList, response.artList)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun `toDomain throws an UnsupportedOperationException`() {
        val randomResponse = randomApiResponse()
        mapper.toDomain(randomResponse)
    }


    private fun stubMapper(artworkList: List<ArtsyArtworkResponse>): List<Art> {
        val mappedList = artworkList.map { randomArt() }

        artworkList.forEachIndexed { index, response ->
            whenever(mockMapper.toModel(response)).thenReturn(mappedList[index])
        }

        return mappedList
    }
}