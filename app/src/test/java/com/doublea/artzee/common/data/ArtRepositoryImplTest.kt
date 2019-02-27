package com.doublea.artzee.common.data

import com.doublea.artzee.common.db.ArtsyCache
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.network.ArtApi
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
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
    lateinit var mockCache: ArtsyCache

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
    fun `when getArtFeed is called, it calls getArtFeed on the pagedListBuilder with the disposable`() {
        whenever(mockCache.getArtFeed(disposable))
            .thenReturn(Flowable.just(mockPagedList))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)

        repository.getArtFeed(disposable).test()

        verify(mockCache).getArtFeed(disposable)
    }

    @Test
    fun `when getArtFeed is called, it returns the pagedList from the pagedListBuilder`() {
        whenever(mockCache.getArtFeed(disposable))
            .thenReturn(Flowable.just(mockPagedList))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)

        val testSubscriber = repository.getArtFeed(disposable).test()
        scheduler.triggerActions()

        testSubscriber.assertValue(mockPagedList)
    }

    @Test
    fun `when getArtistForArtwork is called and artistId is null, it calls getArtistForArtwork on the api with the artwork ID`() {
        val artist = randomArtist()
        val art = randomArt(artistId = null)
        whenever(mockApi.getArtistForArtwork(art.id))
            .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)
        repository.getArtistForArtwork(art)

        verify(mockApi).getArtistForArtwork(art.id)
    }

    @Test
    fun `when getArtistForArtwork is called and artistId is null, it returns the artist returned by the api`() {
        val artist = randomArtist()
        val art = randomArt(artistId = null)

        whenever(mockApi.getArtistForArtwork(art.id))
            .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)

        val testSubscriber = repository
            .getArtistForArtwork(art)
            .test()

        scheduler.triggerActions()

        testSubscriber.assertValue(artist)
    }

    @Test
    fun `when getArtistForArtwork is called and artistId is null, it calls insertArtist on the cache with artist returned by the api and the art ID`() {
        val artist = randomArtist()
        val art = randomArt(artistId = null)

        whenever(mockApi.getArtistForArtwork(art.id))
            .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)
        repository.getArtistForArtwork(art).test()
        scheduler.triggerActions()

        verify(mockCache).insertArtist(artist, art.id)
    }

    @Test
    fun `when getArtistForArtwork is called and artistId is present, it calls getArtistById on the cache with the artistId`() {
        val artist = randomArtist()
        val art = randomArt(artistId = artist.id)
        whenever(mockCache.getArtistById(artist.id))
            .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)
        repository.getArtistForArtwork(art)

        verify(mockCache).getArtistById(artist.id)
    }

    @Test
    fun `when getArtistForArtwork is called and artistId is present, it returns the artist returned by the cache`() {
        val artist = randomArtist()
        val art = randomArt(artistId = artist.id)
        whenever(mockCache.getArtistById(artist.id))
            .thenReturn(Single.just(artist))

        repository = ArtRepositoryImpl(mockApi, mockCache, scheduler)

        val testSubscriber = repository
            .getArtistForArtwork(art)
            .test()

        scheduler.triggerActions()

        testSubscriber.assertValue(artist)
    }
}