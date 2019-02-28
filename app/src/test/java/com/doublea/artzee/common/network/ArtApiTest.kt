package com.doublea.artzee.common.network

import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.network.retrofit.ArtsyService
import com.doublea.artzee.test.data.ArtDataFactory
import com.doublea.artzee.test.data.ArtsyApiDataFactory
import com.doublea.artzee.test.data.TestDataFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
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
    lateinit var mockArtistMapper: Mapper<ArtsyArtistsWrapper, ArtistApiResponse>

    @Mock
    lateinit var mockNetworkHelper: NetworkHelper

    private lateinit var api: ArtApi

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        whenever(mockNetworkHelper.observeNetworkConnectivity())
            .thenReturn(Flowable.just(true))

        api = ArtApi(mockService, mockArtMapper, mockArtistMapper, mockNetworkHelper)
    }

    @Test
    fun `when getArt is called without a cursor, it calls getArt on the artsy service`() {
        stubRandomArtResponse()

        api.getArt().test()

        verify(mockService).getArt()
    }

    @Test
    fun `when getArt is called with a blank cursor, it calls getArt on the artsy service`() {
        stubRandomArtResponse()

        api.getArt(" ").test()

        verify(mockService).getArt()
    }

    @Test
    fun `when getArt is called with a cursor, it calls getArtByCursor on the artsy service`() {
        stubRandomArtResponse()

        val cursor = TestDataFactory.randomString()

        api.getArt(cursor).test()

        verify(mockService).getArtByCursor(cursor)
    }

    @Test
    fun `when getArt is called, it calls the art mapper with the artsy service response`() {
        val response = stubRandomArtResponse()

        api.getArt().test()

        verify(mockArtMapper).toModel(response)
    }

    @Test
    fun `when getArt is called, it returns the response from the art mapper`() {
        stubRandomArtResponse()

        val mockApiResponse = ArtDataFactory.randomApiResponse()
        whenever(mockArtMapper.toModel(any())).thenReturn(mockApiResponse)

        val testObserver = api.getArt().test()

        testObserver.assertValue(mockApiResponse)
    }

    @Test
    fun `when getArtistForArtwork is called, it calls the artist mapper with the artsy service response`() {
        val response = stubRandomArtistResponse()

        api.getArtistForArtwork(TestDataFactory.randomString()).test()

        verify(mockArtistMapper).toModel(response)
    }

    @Test
    fun `when getArtistForArtwork is called, it calls getArtistsByArtworkId with the proper id`() {
        stubRandomArtistResponse()

        val randomId = TestDataFactory.randomString()

        api.getArtistForArtwork(randomId).test()

        verify(mockService).getArtistsByArtworkId(randomId)
    }

    @Test
    fun `when getArtistForArtwork is called, it returns the response from the artist mapper`() {
        stubRandomArtistResponse()

        val mockArtist = ArtDataFactory.randomArtist()
        val response = ArtistApiResponse(mockArtist)

        whenever(mockArtistMapper.toModel(any())).thenReturn(response)

        val testObserver = api.getArtistForArtwork(TestDataFactory.randomString()).test()

        testObserver.assertValue(response)
    }

    @Test
    fun `when there is no network connectivity, it does not make an api call`() {
        stubRandomArtResponse()

        whenever(mockNetworkHelper.observeNetworkConnectivity())
            .thenReturn(Flowable.just(false))

        api.getArt().test()

        verifyZeroInteractions(mockService)
    }

    @Test
    fun `when the network connectivity changes from false to true, it makes an api call`() {
        stubRandomArtResponse()

        val subject = BehaviorSubject.create<Boolean>()
        subject.onNext(false)

        whenever(mockNetworkHelper.observeNetworkConnectivity())
            .thenReturn(subject.toFlowable(BackpressureStrategy.DROP))

        api.getArt().test()

        verifyZeroInteractions(mockService)

        subject.onNext(true)

        verify(mockService).getArt()
    }

    private fun stubRandomArtResponse(): ArtsyArtworkWrapper {
        val mockResponse = ArtsyApiDataFactory.randomArtworkWrapper()
        whenever(mockService.getArt()).thenReturn(
            Single.just(mockResponse)
        )
        return mockResponse
    }

    private fun stubRandomArtistResponse(): ArtsyArtistsWrapper {
        val mockResponse = ArtsyApiDataFactory.randomArtistsWrapper()
        whenever(mockService.getArtistsByArtworkId(any())).thenReturn(
            Single.just(mockResponse)
        )
        return mockResponse
    }
}