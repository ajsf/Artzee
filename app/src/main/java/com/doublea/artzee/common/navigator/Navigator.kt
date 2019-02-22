package com.doublea.artzee.common.navigator

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.artdetail.view.ArtDetailTextFragment

interface Navigator {
    fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int)
}

class NavigatorImpl(private val fragmentActivity: FragmentActivity) : Navigator {

    override fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int) {
        val fragmentManager = fragmentActivity.supportFragmentManager
        ArtDetailFragment.launch(fragmentManager, artId, sharedViews)
        ArtDetailTextFragment.launch(fragmentManager, colorId)
    }
}
