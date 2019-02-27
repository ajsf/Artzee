package com.doublea.artzee.common.data

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.test.data.ArtDataFactory
import com.doublea.artzee.test.data.TestDataFactory
import com.doublea.artzee.test.data.TestDataFactory.randomInt
import com.doublea.artzee.test.data.TestDataFactory.randomList
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class ArtBoundaryCallbackTest {

    @Mock
    lateinit var mockApi: ArtApi

    @Mock
    lateinit var mockCache: ArtsyCache

    @Mock
    lateinit var mockPrefs: PreferencesHelper

    @Mock
    lateinit var mockMapper: Mapper<ArtApiResponse, List<ArtEntity>>

    private lateinit var artBoundaryCallback: ArtBoundaryCallback

    private lateinit var disposable: CompositeDisposable

    private lateinit var scheduler: TestScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stubCacheSuccess()

        disposable = CompositeDisposable()
        scheduler = TestScheduler()
        artBoundaryCallback = ArtBoundaryCallback(mockApi, mockPrefs, mockMapper, scheduler)
        artBoundaryCallback.compositeDisposable = disposable
        artBoundaryCallback.cache = mockCache
    }

    @After
    fun dispose() = disposable.dispose()

    @Test
    fun `when onZeroItemsLoaded is called, it calls getArt on the api`() {
        stubRandomMapperResponse(stubRandomApiResponse())

        callOnZeroItemsLoaded()

        verify(mockApi).getArt()
    }

    @Test
    fun `when onZeroItemsLoaded is called, it calls toModel on the mapper with the api response`() {
        val randomResponse = stubRandomApiResponse()
        stubRandomMapperResponse(randomResponse)

        callOnZeroItemsLoaded()

        verify(mockMapper).toModel(randomResponse)
    }

    @Test
    fun `when onZeroItemsLoaded is called, it calls insert on the cache with the art list from the api response`() {
        val randomResponse = stubRandomApiResponse()
        val randomEntities = stubRandomMapperResponse(randomResponse)

        callOnZeroItemsLoaded()

        verify(mockCache).insertArtworks(eq(randomEntities))
    }

    @Test
    fun `when onZeroItemsLoaded is called and the cache insert completes, it calls saveCursor on the preferences helper with the cursor from the api response`() {
        val randomResponse = stubRandomApiResponse()
        stubRandomMapperResponse(randomResponse)

        callOnZeroItemsLoaded()

        verify(mockPrefs).saveCursor(randomResponse.cursor)
    }

    @Test
    fun `when onZeroItemsLoaded is called and the cache insert has an error, saveCursor is not called`() {
        stubRandomMapperResponse(stubRandomApiResponse())
        stubCacheError()

        callOnZeroItemsLoaded()

        verify(mockPrefs, never()).saveCursor(any())
    }

    @Test
    fun `it only calls the api once if the cache insert from the first call hasn't completed`() {
        stubRandomMapperResponse(stubRandomApiResponse())
        stubNoCacheResponse()

        repeat(randomInt()) { callOnZeroItemsLoaded() }

        verify(mockApi, times(1)).getArt()
    }

    @Test
    fun `once the first cache insert completes, it calls getArt again on the next request`() {
        stubRandomMapperResponse(stubRandomApiResponse())
        stubCacheDelay(1000)

        repeat(randomInt()) { callOnZeroItemsLoaded() }

        verify(mockApi, times(1)).getArt()

        scheduler.advanceTimeBy(2000, TimeUnit.MILLISECONDS)

        callOnZeroItemsLoaded()

        verify(mockApi, times(2)).getArt()
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls getCursor on the preferences helper`() {
        stubRandomMapperResponse(stubRandomApiResponse())
        stubRandomCursorResponse()

        callOnItemAtEndLoaded()

        verify(mockPrefs).getCursor()
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls getArt on the api with the cursor from the preferences helper`() {
        stubRandomMapperResponse(stubRandomApiResponse())
        val randomCursor = stubRandomCursorResponse()

        callOnItemAtEndLoaded()

        verify(mockApi).getArt(randomCursor)
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls insert on the cache with the art list from the api response`() {
        val randomResponse = stubRandomApiResponse()
        val randomEntities = stubRandomMapperResponse(randomResponse)
        stubRandomCursorResponse()

        callOnItemAtEndLoaded()

        verify(mockCache).insertArtworks(eq(randomEntities))
    }

    @Test
    fun `when onItemAtAtEndLoaded is called and the cache insert completes, it calls saveCursor on the preferences helper with the cursor from the api response`() {
        stubRandomCursorResponse()
        val randomResponse = stubRandomApiResponse()
        stubRandomMapperResponse(randomResponse)
        callOnItemAtEndLoaded()

        verify(mockPrefs).saveCursor(randomResponse.cursor)
    }

    @Test
    fun `when onItemAtEndLoaded is called and the cache insert has an error, saveCursor is not called`() {
        stubRandomCursorResponse()
        stubRandomMapperResponse(stubRandomApiResponse())
        stubCacheError()

        callOnItemAtEndLoaded()

        verify(mockPrefs, never()).saveCursor(any())
    }

    @Test
    fun `if onItemAtEndLoaded is called before onZeroItemsLoaded completes, getArt is only called once`() {
        stubRandomCursorResponse()
        stubRandomMapperResponse(stubRandomApiResponse())
        stubNoCacheResponse()

        callOnZeroItemsLoaded()
        callOnItemAtEndLoaded()

        verify(mockApi, times(1)).getArt(any())
    }

    @Test
    fun `once onZeroItemsLoaded completes, and onItemAtEndLoaded is called, getArt is called again`() {
        stubRandomCursorResponse()
        stubRandomMapperResponse(stubRandomApiResponse())
        stubCacheDelay(1000)

        callOnZeroItemsLoaded()

        scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)

        callOnItemAtEndLoaded()

        verify(mockApi, times(1)).getArt(any())

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)

        callOnItemAtEndLoaded()

        verify(mockApi, times(2)).getArt(any())
    }

    private fun stubRandomApiResponse(): ArtApiResponse {
        val mockResponse = ArtDataFactory.randomApiResponse()
        whenever(mockApi.getArt(any()))
            .thenReturn(Single.just(mockResponse))
        return mockResponse
    }

    private fun stubRandomMapperResponse(apiResponse: ArtApiResponse): List<ArtEntity> {
        val mockEntityList = randomList(ArtDataFactory::randomArtEntity)
        whenever(mockMapper.toModel(apiResponse))
            .thenReturn(mockEntityList)
        return mockEntityList
    }

    private fun stubRandomCursorResponse(): String {
        val randomCursor = TestDataFactory.randomString()
        whenever(mockPrefs.getCursor())
            .thenReturn(randomCursor)
        return randomCursor
    }

    private fun callOnZeroItemsLoaded() {
        artBoundaryCallback.onZeroItemsLoaded()
        scheduler.triggerActions()
    }

    private fun callOnItemAtEndLoaded() {
        artBoundaryCallback.onItemAtEndLoaded(ArtDataFactory.randomArt())
        scheduler.triggerActions()
    }

    private fun stubCacheError() {
        whenever(mockCache.insertArtworks(any()))
            .thenReturn(Completable.error(Throwable()))
    }

    private fun stubCacheSuccess() {
        whenever(mockCache.insertArtworks(any()))
            .thenReturn(Completable.complete())
    }

    private fun stubNoCacheResponse() {
        whenever(mockCache.insertArtworks(any()))
            .thenReturn(Completable.never())
    }

    private fun stubCacheDelay(timeMs: Long) {
        whenever(mockCache.insertArtworks(any()))
            .thenReturn(Completable.timer(timeMs, TimeUnit.MILLISECONDS, scheduler))
    }

}