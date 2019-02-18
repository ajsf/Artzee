package com.doublea.artzee.browse.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import com.doublea.artzee.common.data.ArtRepository
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.navigator.Navigator
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BrowseArtViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockRepository: ArtRepository

    @Mock
    lateinit var mockNavigator: Navigator

    @Mock
    lateinit var pagedList: PagedList<Art>

    private lateinit var artPagedList: ArtPagedList

    private lateinit var viewModel: BrowseArtViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        artPagedList = ArtPagedList(pagedList)
    }

    @Test
    fun `it calls getArtFeed on the repository when it's created`() {
        viewModel = BrowseArtViewModel(mockRepository, mockNavigator)
        verify(mockRepository).getArtFeed(any())
    }

    @Test
    fun `the artList feed sends the artPagedList returned from the repository`() {
        whenever(mockRepository.getArtFeed(any()))
            .thenReturn(Flowable.just(artPagedList))

        viewModel = BrowseArtViewModel(mockRepository, mockNavigator)

        var returnedList: ArtPagedList? = null
        viewModel.artList
            .observeForever { returnedList = it }

        assertEquals(artPagedList, returnedList)
    }

    @Test
    fun `calling selectArtItem calls viewArtDetail on the navigator`() {
        viewModel = BrowseArtViewModel(mockRepository, mockNavigator)

        val art = randomArt()
        viewModel.selectArtItem(art.id)

        verify(mockNavigator).viewArtDetail(art.id)
    }
}