package com.doublea.artzee.common.navigator

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.test.data.TestDataFactory.randomString
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
    lateinit var mockActivity: FragmentActivity

    @Mock
    lateinit var mockFragmentManager: FragmentManager

    @Mock
    lateinit var mockTransaction: FragmentTransaction

    @Mock
    lateinit var mockImageView: ImageView

    private lateinit var randomArtId: String

    private lateinit var navigator: NavigatorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        navigator = NavigatorImpl(mockActivity)

        whenever(mockActivity.supportFragmentManager)
            .thenReturn(mockFragmentManager)

        whenever(mockFragmentManager.beginTransaction())
            .thenReturn(mockTransaction)

        randomArtId = randomString()
    }

    @Test
    fun `calling viewArtDetail calls beginTransaction on the fragment manager`() {
        navigator.viewArtDetail(randomArtId, mockImageView)
        verify(mockFragmentManager).beginTransaction()
    }

    @Test
    fun `calling viewArtDetail calls replace on the transaction with an ArtDetailFragment`() {
        navigator.viewArtDetail(randomArtId, mockImageView)
        verify(mockTransaction).replace(any(), isA<ArtDetailFragment>(), any())
    }

    @Test
    fun `calling viewArtDetail calls addToBackStack on the transaction`() {
        navigator.viewArtDetail(randomArtId, mockImageView)
        verify(mockTransaction).addToBackStack(null)
    }

    @Test
    fun `calling viewArtDetail adds the ImageView as a shared element to the transaction, with the transitionId returned from the ImageView`() {
        val transitionId = randomString()
        whenever(mockImageView.transitionName).thenReturn(transitionId)

        navigator.viewArtDetail(randomArtId, mockImageView)

        verify(mockTransaction).addSharedElement(mockImageView, transitionId)
    }

    @Test
    fun `calling viewArtDetail calls setReorderingAllowed(true) on the transaction`() {
        navigator.viewArtDetail(randomArtId, mockImageView)
        verify(mockTransaction).setReorderingAllowed(true)
    }

    @Test
    fun `calling viewArtDetail calls commit on the transaction`() {
        navigator.viewArtDetail(randomArtId, mockImageView)
        verify(mockTransaction).commit()
    }
}