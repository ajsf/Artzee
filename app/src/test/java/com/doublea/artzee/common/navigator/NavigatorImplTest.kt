package com.doublea.artzee.common.navigator

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.artdetail.view.ArtDetailTextFragment
import com.doublea.artzee.test.data.TestDataFactory.randomInt
import com.doublea.artzee.test.data.TestDataFactory.randomString
import com.nhaarman.mockitokotlin2.*
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
    lateinit var firstTransaction: FragmentTransaction

    @Mock
    lateinit var secondTransaction: FragmentTransaction

    @Mock
    lateinit var mockView: ImageView

    private lateinit var viewList: List<View>

    private lateinit var randomArtId: String

    private var randomColorId: Int = 0

    private lateinit var navigator: NavigatorImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        whenever(mockActivity.supportFragmentManager)
            .thenReturn(mockFragmentManager)

        whenever(mockFragmentManager.beginTransaction())
            .thenReturn(firstTransaction)
            .thenReturn(secondTransaction)

        viewList = listOf(mockView)
        randomArtId = randomString()
        randomColorId = randomInt()
        navigator = NavigatorImpl(mockActivity)
    }

    @Test
    fun `calling viewArtDetail calls beginTransaction on the fragment manager twice`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(mockFragmentManager, times(2)).beginTransaction()
    }

    @Test
    fun `calling viewArtDetail calls replace on the first transaction with an ArtDetailFragment`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(firstTransaction).replace(any(), isA<ArtDetailFragment>(), eq(null))
    }

    @Test
    fun `calling viewArtDetail calls replace on the second transaction with an ArtDetailTextFragment`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(secondTransaction).replace(any(), isA<ArtDetailTextFragment>(), eq(null))
    }

    @Test
    fun `calling viewArtDetail calls addToBackStack on the first transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(firstTransaction).addToBackStack(null)
    }

    @Test
    fun `calling viewArtDetail calls addToBackStack on the second transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(secondTransaction).addToBackStack(null)
    }

    @Test
    fun `calling viewArtDetail adds the ImageView as a shared element to the first transaction, with the transitionId returned from the ImageView`() {
        val transitionId = randomString()
        whenever(mockView.transitionName).thenReturn(transitionId)

        navigator.viewArtDetail(randomArtId, viewList, randomColorId)

        verify(firstTransaction).addSharedElement(mockView, transitionId)
    }

    @Test
    fun `calling viewArtDetail calls setReorderingAllowed(true) on the first transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(firstTransaction).setReorderingAllowed(true)
    }

    @Test
    fun `calling viewArtDetail calls setReorderingAllowed(true) on the second transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(secondTransaction).setReorderingAllowed(true)
    }

    @Test
    fun `calling viewArtDetail calls commit on the first transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(firstTransaction).commit()
    }

    @Test
    fun `calling viewArtDetail calls commit on the second transaction`() {
        navigator.viewArtDetail(randomArtId, viewList, randomColorId)
        verify(secondTransaction).commit()
    }
}