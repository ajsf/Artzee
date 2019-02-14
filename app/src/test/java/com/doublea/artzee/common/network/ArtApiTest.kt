package com.doublea.artzee.common.network

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.common.network.retrofit.ArtsyService
import com.doublea.artzee.test.data.ArtDataFactory
import com.doublea.artzee.test.data.ArtsyApiDataFactory
import com.doublea.artzee.test.data.TestDataFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class ArtApiTest {

    @Mock
    lateinit var mockService: ArtsyService

    @Mock
    lateinit var mockArtMapper: Mapper<ArtsyArtworkWrapper, ArtApiResponse>

    @Mock
    lateinit var mockArtistMapper: Mapper<ArtsyArtistsWrapper, Artist>

    private lateinit var api: ArtApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        api = ArtApi(mockService, mockArtMapper, mockArtistMapper)
    }

    @Test
    fun `when getArt is called without a cursor, it calls getArt on the artsy service`() {
        val mockResponse = ArtsyApiDataFactory.randomArtworkWrapper()
        whenever(mockService.getArt()).thenReturn(
                Single.just(mockResponse))

        api.getArt().test()

        verify(mockService).getArt()
    }

    @Test
    fun `when getArt is called with a blank cursor, it calls getArt on the artsy service`() {
        val mockResponse = ArtsyApiDataFactory.randomArtworkWrapper()
        whenever(mockService.getArt()).thenReturn(
                Single.just(mockResponse))

        api.getArt(" ").test()

        verify(mockService).getArt()
    }

    @Test
    fun `when getArt is called with a cursor, it calls getArtByCursor on the artsy service`() {
        val mockResponse = ArtsyApiDataFactory.randomArtworkWrapper()

        whenever(mockService.getArtByCursor(any(), any())).thenReturn(
                Single.just(mockResponse))

        val cursor = TestDataFactory.randomString()

        api.getArt(cursor).test()

        verify(mockService).getArtByCursor(cursor)
    }

    @Test
    fun `when getArt is called, it calls the art mapper with the artsy service response`() {
        val mockResponse = ArtsyApiDataFactory.randomArtworkWrapper()

        whenever(mockService.getArt()).thenReturn(
                Single.just(mockResponse))

        api.getArt().test()

        verify(mockArtMapper).toModel(mockResponse)
    }

    @Test
    fun `when getArt is called, it returns the response from the art mapper`() {

        val mockResponse = ArtDataFactory.randomApiResponse()

        whenever(mockService.getArt(any())).thenReturn(
                Single.just(ArtsyApiDataFactory.randomArtworkWrapper()))

        whenever(mockArtMapper.toModel(any())).thenReturn(mockResponse)

        val testObserver = api.getArt().test()

        testObserver.assertValue(mockResponse)
    }

    @Test
    fun `when getArtistForArtwork is called, it calls the artist mapper with the artsy service response`() {
        val mockResponse = ArtsyApiDataFactory.randomArtistsWrapper()

        whenever(mockService.getArtistsByArtworkId(any())).thenReturn(
                Single.just(mockResponse))

        api.getArtistForArtwork(TestDataFactory.randomString()).test()

        verify(mockArtistMapper).toModel(mockResponse)
    }

    @Test
    fun `when getArtistForArtwork is called, it calls getArtistsByArtworkId with the proper id`() {
        val mockResponse = ArtsyApiDataFactory.randomArtistsWrapper()

        whenever(mockService.getArtistsByArtworkId(any())).thenReturn(
                Single.just(mockResponse))

        val randomId = TestDataFactory.randomString()

        api.getArtistForArtwork(randomId).test()

        verify(mockService).getArtistsByArtworkId(randomId)
    }

    @Test
    fun `when getArtistForArtwork is called, it returns the response from the artist mapper`() {
        val mockResponse = ArtsyApiDataFactory.randomArtistsWrapper()

        whenever(mockService.getArtistsByArtworkId(any())).thenReturn(
                Single.just(mockResponse))

        val mockArtist = ArtDataFactory.randomArtist()

        whenever(mockArtistMapper.toModel(any())).thenReturn(mockArtist)

        val testObserver = api.getArtistForArtwork(TestDataFactory.randomString()).test()

        testObserver.assertValue(mockArtist)
    }
}