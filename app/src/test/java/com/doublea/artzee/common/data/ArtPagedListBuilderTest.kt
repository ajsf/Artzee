package com.doublea.artzee.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.doublea.artzee.test.data.TestDataFactory.randomList
import com.doublea.artzee.test.data.TestDataSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArtPagedListBuilderTest {

    @get:Rule
    val rul = InstantTaskExecutorRule()

    @Mock
    lateinit var mockCallback: ArtBoundaryCallback

    @Mock
    lateinit var mockCache: ArtsyCache

    private lateinit var artPagedListBuilder: ArtPagedListBuilder

    private lateinit var disposable: CompositeDisposable

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        artPagedListBuilder = ArtPagedListBuilder(mockCallback)
        disposable = CompositeDisposable()
    }

    @Test
    fun `when getPagedList is called it sets the disposable on the callback`() {
        whenever(mockCache.allArt())
            .thenReturn(TestDataSourceFactory(randomList({ randomArt() })))

        artPagedListBuilder.getPagedList(mockCache, disposable)

        verify(mockCallback).compositeDisposable = disposable
    }

    @Test
    fun `when getPagedList is called it calls allArt on the cache`() {
        whenever(mockCache.allArt())
            .thenReturn(TestDataSourceFactory(randomList({ randomArt() })))

        artPagedListBuilder.getPagedList(mockCache, disposable)

        verify(mockCache).allArt()
    }

    @Test
    fun `when getPagedList is subscribed to, it returns the list from the cache`() {
        val randomList = randomList({ randomArt() })
        whenever(mockCache.allArt())
            .thenReturn(TestDataSourceFactory(randomList))

        val testSubscriber = artPagedListBuilder.getPagedList(mockCache, disposable).test()
        val pagedList = testSubscriber.values().first()

        assertEquals(randomList, pagedList.list)
    }
}