package com.doublea.artzee.common.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import com.doublea.artzee.common.data.ArtPagedListBuilder
import com.doublea.artzee.common.db.room.ArtDao
import com.doublea.artzee.common.db.room.ArtEntity
import com.doublea.artzee.common.mapper.Mapper
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.test.data.ArtDataFactory.randomArtEntity
import com.doublea.artzee.test.data.TestDataFactory.randomList
import com.doublea.artzee.test.data.TestDataSourceFactory
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
    lateinit var mockDao: ArtDao

    @Mock
    lateinit var mockMapper: Mapper<ArtEntity, Art>

    @Mock
    lateinit var mockPagedListBuilder: ArtPagedListBuilder

    private lateinit var dataSource: DataSource.Factory<Int, ArtEntity>

    private lateinit var cache: ArtsyCache

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dataSource = TestDataSourceFactory(randomList(::randomArtEntity))
        whenever(mockDao.getAllArt()).thenReturn(dataSource)
        cache = ArtsyCache(mockDao, mockMapper, mockPagedListBuilder)
    }

    @Test
    fun `when allArt is called, it calls getAllArt on the dao`() {
        val d = cache.allArt()
        d.map { println("mapping") }
        Thread.sleep(2000)
        verify(mockDao).getAllArt()

    }

}