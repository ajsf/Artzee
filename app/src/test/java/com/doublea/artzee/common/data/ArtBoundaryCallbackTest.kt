package com.doublea.artzee.common.data

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.common.network.ArtApiResponse
import com.doublea.artzee.test.data.ArtDataFactory
import com.doublea.artzee.test.data.TestDataFactory
import com.doublea.artzee.test.data.TestDataFactory.randomInt
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

    private lateinit var artBoundaryCallback: ArtBoundaryCallback

    private lateinit var disposable: CompositeDisposable

    private lateinit var scheduler: TestScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stubCacheSuccess()

        disposable = CompositeDisposable()
        scheduler = TestScheduler()
        artBoundaryCallback = ArtBoundaryCallback(mockApi, mockPrefs, scheduler)
        artBoundaryCallback.compositeDisposable = disposable
        artBoundaryCallback.cache = mockCache
    }

    @After
    fun dispose() = disposable.dispose()

    @Test
    fun `when onZeroItemsLoaded is called, it calls getArt on the api`() {
        stubRandomApiResponse()

        callOnZeroItemsLoaded()

        verify(mockApi).getArt()
    }

    @Test
    fun `when onZeroItemsLoaded is called, it calls insert on the cache with the art list from the api response`() {
        val mockResponse = stubRandomApiResponse()

        callOnZeroItemsLoaded()

        verify(mockCache).insert(eq(mockResponse.artList))
    }

    @Test
    fun `when onZeroItemsLoaded is called and the cache insert completes, it calls saveCursor on the preferences helper with the cursor from the api response`() {
        val mockResponse = stubRandomApiResponse()

        callOnZeroItemsLoaded()

        verify(mockPrefs).saveCursor(mockResponse.cursor)
    }

    @Test
    fun `when onZeroItemsLoaded is called and the cache insert has an error, saveCursor is not called`() {
        stubRandomApiResponse()
        stubCacheError()

        callOnZeroItemsLoaded()

        verify(mockPrefs, never()).saveCursor(any())
    }

    @Test
    fun `it only calls the api once if the cache insert from the first call hasn't completed`() {
        stubRandomApiResponse()
        stubNoCacheResponse()

        repeat(randomInt()) { callOnZeroItemsLoaded() }

        verify(mockApi, times(1)).getArt()
    }

    @Test
    fun `once the first cache insert completes, it calls getArt again on the next request`() {
        stubRandomApiResponse()
        stubCacheDelay(1000)

        repeat(randomInt()) { callOnZeroItemsLoaded() }

        verify(mockApi, times(1)).getArt()

        scheduler.advanceTimeBy(2000, TimeUnit.MILLISECONDS)

        callOnZeroItemsLoaded()

        verify(mockApi, times(2)).getArt()
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls getCursor on the preferences helper`() {
        stubRandomCursorResponse()
        stubRandomApiResponse()

        callOnItemAtEndLoaded()

        verify(mockPrefs).getCursor()
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls getArt on the api with the cursor from the preferences helper`() {
        stubRandomApiResponse()
        val randomCursor = stubRandomCursorResponse()

        callOnItemAtEndLoaded()

        verify(mockApi).getArt(randomCursor)
    }

    @Test
    fun `when onItemAtEndLoaded is called, it calls insert on the cache with the art list from the api response`() {
        stubRandomCursorResponse()
        val mockResponse = stubRandomApiResponse()

        callOnItemAtEndLoaded()

        verify(mockCache).insert(eq(mockResponse.artList))
    }

    @Test
    fun `when onItemAtAtEndLoaded is called and the cache insert completes, it calls saveCursor on the preferences helper with the cursor from the api response`() {
        stubRandomCursorResponse()
        val mockResponse = stubRandomApiResponse()

        callOnItemAtEndLoaded()

        verify(mockPrefs).saveCursor(mockResponse.cursor)
    }

    @Test
    fun `when onItemAtEndLoaded is called and the cache insert has an error, saveCursor is not called`() {
        stubRandomCursorResponse()
        stubRandomApiResponse()
        stubCacheError()

        callOnItemAtEndLoaded()

        verify(mockPrefs, never()).saveCursor(any())
    }

    @Test
    fun `if onItemAtEndLoaded is called before onZeroItemsLoaded completes, getArt is only called once`() {
        stubRandomCursorResponse()
        stubRandomApiResponse()
        stubNoCacheResponse()

        callOnZeroItemsLoaded()
        callOnItemAtEndLoaded()

        verify(mockApi, times(1)).getArt(any())
    }

    @Test
    fun `once onZeroItemsLoaded completes, and onItemAtEndLoaded is called, getArt is called again`() {
        stubRandomCursorResponse()
        stubRandomApiResponse()
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
        whenever(mockCache.insert(any()))
            .thenReturn(Completable.error(Throwable()))
    }

    private fun stubCacheSuccess() {
        whenever(mockCache.insert(any()))
            .thenReturn(Completable.complete())
    }

    private fun stubNoCacheResponse() {
        whenever(mockCache.insert(any()))
            .thenReturn(Completable.never())
    }

    private fun stubCacheDelay(timeMs: Long) {
        whenever(mockCache.insert(any()))
            .thenReturn(Completable.timer(timeMs, TimeUnit.MILLISECONDS, scheduler))
    }

}