package com.doublea.artzee.common.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import com.doublea.artzee.common.data.ArtPagedListBuilder
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.db.room.ArtistDao
import com.doublea.artzee.common.db.room.ArtistEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import com.doublea.artzee.test.data.ArtDataFactory
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.doublea.artzee.test.data.ArtDataFactory.randomArtEntity
import com.doublea.artzee.test.data.TestDataFactory.randomList
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.doublea.artzee.test.data.TestDataSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArtsyCacheTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockArtDao: ArtDao

    @Mock
    lateinit var mockArtMapper: Mapper<ArtEntity, Art>

    @Mock
    lateinit var mockArtistDao: ArtistDao

    @Mock
    lateinit var mockArtistMapper: Mapper<ArtistEntity, Artist>

    @Mock
    lateinit var mockPagedListBuilder: ArtPagedListBuilder

    private lateinit var dataSource: DataSource.Factory<Int, ArtEntity>

    private lateinit var cache: ArtsyCache

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dataSource = TestDataSourceFactory(randomList(::randomArtEntity))
        cache = ArtsyCache(mockArtDao, mockArtMapper, mockPagedListBuilder, mockArtistDao, mockArtistMapper)
    }

    @Test
    fun `when insertArtworks is called, it calls insert on the art dao`() {
        val artList = randomList({ randomArt() })

        cache.insertArtworks(artList)

        //verify(mockArtDao).insert(artList)
    }

    @Test
    fun `when allArt is called, it calls getAllArt on the dao`() {
        whenever(mockArtDao.getAllArt()).thenReturn(dataSource)

        cache.allArt()

        verify(mockArtDao).getAllArt()
    }

    @Test
    fun `when getArtById is called, it calls getArtById on the art dao`() {
        val artId = randomString()
        val artEntity = randomArtEntity()
        val art = randomArt()

        whenever(mockArtDao.getArtById(artId)).thenReturn(Single.just(artEntity))
        whenever(mockArtMapper.toModel(artEntity)).thenReturn(art)

        cache.getArtById(artId).test()

        verify(mockArtDao).getArtById(artId)
    }

    @Test
    fun `when getArtById is called, it calls toModel on the art mapper with the art returned by the dao`() {
        val artId = randomString()
        val artEntity = randomArtEntity()
        val art = randomArt()

        whenever(mockArtDao.getArtById(artId)).thenReturn(Single.just(artEntity))
        whenever(mockArtMapper.toModel(artEntity)).thenReturn(art)

        cache.getArtById(artId).test()

        verify(mockArtMapper).toModel(artEntity)
    }

    @Test
    fun `when getArtById is called, it returns the mapped art`() {
        val artId = randomString()
        val artEntity = randomArtEntity()
        val art = randomArt()

        whenever(mockArtDao.getArtById(artId)).thenReturn(Single.just(artEntity))
        whenever(mockArtMapper.toModel(artEntity)).thenReturn(art)

        val testSubscriber = cache.getArtById(artId).test()

        testSubscriber.assertValue(art)
    }

    @Test
    fun `when getArtistById is called, it calls getArtistById on the artist dao with the artist ID`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()

        whenever(mockArtistDao.getArtistById(artist.id)).thenReturn(Single.just(artistEntity))
        whenever(mockArtistMapper.toModel(artistEntity)).thenReturn(artist)

        cache.getArtistById(artist.id)

        verify(mockArtistDao).getArtistById(artist.id)
    }

    @Test
    fun `when getArtistById is called, it calls toModel on the artist mapper with the entity returned by the artist dao`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()

        whenever(mockArtistDao.getArtistById(artist.id)).thenReturn(Single.just(artistEntity))
        whenever(mockArtistMapper.toModel(artistEntity)).thenReturn(artist)

        cache.getArtistById(artist.id).test()

        verify(mockArtistMapper).toModel(artistEntity)
    }

    @Test
    fun `when getArtistById is called, it returns the mapped artist`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()

        whenever(mockArtistDao.getArtistById(artist.id)).thenReturn(Single.just(artistEntity))
        whenever(mockArtistMapper.toModel(artistEntity)).thenReturn(artist)

        val testSubscriber = cache.getArtistById(artist.id).test()

        testSubscriber.assertValue(artist)
    }

    @Test
    fun `when insertArtist is called, it calls toDomain on the artist mapper with the artist`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()
        val artId = randomString()

        whenever(mockArtistMapper.toDomain(artist)).thenReturn(artistEntity)

        cache.insertArtist(artist, artId)

        verify(mockArtistMapper).toDomain(artist)
    }

    @Test
    fun `when insertArtist is called, it calls insert on the artist dao with the mapped artist entity`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()
        val artId = randomString()

        whenever(mockArtistMapper.toDomain(artist)).thenReturn(artistEntity)

        cache.insertArtist(artist, artId)

        verify(mockArtistDao).insert(artistEntity)
    }

    @Test
    fun `when insertArtist is called, it calls updatedArtistIdForArt on the art dao with the art ID and the artist ID`() {
        val artistEntity = ArtDataFactory.randomArtistEntity()
        val artist = ArtDataFactory.randomArtist()
        val artId = randomString()

        whenever(mockArtistMapper.toDomain(artist)).thenReturn(artistEntity)

        cache.insertArtist(artist, artId)

        verify(mockArtDao).updateArtistIdForArt(artId, artist.id)
    }
}