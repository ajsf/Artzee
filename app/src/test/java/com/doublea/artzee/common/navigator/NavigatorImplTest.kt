package com.doublea.artzee.common.navigator

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.test.data.ArtDataFactory.randomArt
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NavigatorImplTest {

    @Mock
    lateinit var mockFragmentManager: FragmentManager

    @Mock
    lateinit var mockTransaction: FragmentTransaction

    private lateinit var art: Art

    private lateinit var navigator: NavigatorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(mockFragmentManager.beginTransaction()).thenReturn(mockTransaction)
        navigator = NavigatorImpl(mockFragmentManager)
        navigator.viewArtDetail(randomArt())
    }

    @Test
    fun `calling viewArtDetail calls beginTransaction on the fragment manager`() {
        verify(mockFragmentManager).beginTransaction()
    }

    @Test
    fun `calling viewArtDetail calls replace on the transaction with an ArtDetailFragment`() {
        verify(mockTransaction).replace(any(), isA<ArtDetailFragment>(), any())
    }

    @Test
    fun `calling viewArtDetail calls addToBackStack on the transaction`() {
        verify(mockTransaction).addToBackStack(null)
    }

    @Test
    fun `calling viewArtDetail calls commit on the transaction`() {
        verify(mockTransaction).commit()
    }
}