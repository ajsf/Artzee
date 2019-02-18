package com.doublea.artzee.common.data

import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.test.data.ArtDataFactory.randomArtist
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArtRepositoryImplTest {

    @Mock
    lateinit var mockApi: ArtApi

    @Mock
    lateinit var mockPagedListBuilder: ArtPagedListBuilder

    @Mock
    private lateinit var mockPagedList: ArtPagedList

    private lateinit var repository: ArtRepositoryImpl

    private lateinit var disposable: CompositeDisposable

    private lateinit var scheduler: TestScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        disposable = CompositeDisposable()
        scheduler = TestScheduler()
    }

    @Test
    fun `when getArtFeed is called, it calls getPagedList on the pagedListBuilder with the disposable`() {
        whenever(mockPagedListBuilder.getPagedList(disposable))
                .thenReturn(Flowable.just(mockPagedList))

        repository = ArtRepositoryImpl(mockApi, mockPagedListBuilder, scheduler)

        repository.getArtFeed(disposable).test()

        verify(mockPagedListBuilder).getPagedList(disposable)
    }

    @Test
    fun `when getArtFeed is called, it returns the pagedList from the pagedListBuilder`() {
        whenever(mockPagedListBuilder.getPagedList(disposable))
                .thenReturn(Flowable.just(mockPagedList))

        repository = ArtRepositoryImpl(mockApi, mockPagedListBuilder, scheduler)

        val testSubscriber = repository.getArtFeed(disposable).test()

        testSubscriber.assertValue(mockPagedList)
    }

    @Test
    fun `when getArtistForArtwork is called, it calls getArtistForArtwork on the api with the artwork ID`() {
        val artist = randomArtist()
        val randomId = randomString()
        whenever(mockApi.getArtistForArtwork(randomId))
                .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockPagedListBuilder, scheduler)
        repository.getArtistForArtwork(randomId)

        verify(mockApi).getArtistForArtwork(randomId)
    }

    @Test
    fun `when getArtistForArtwork is called it returns the artist returned by the api`() {
        val artist = randomArtist()
        val randomId = randomString()

        whenever(mockApi.getArtistForArtwork(randomId))
                .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockPagedListBuilder, scheduler)

        val testSubscriber = repository
                .getArtistForArtwork(randomId)
                .test()

        scheduler.triggerActions()

        testSubscriber.assertValue(artist)
    }

}