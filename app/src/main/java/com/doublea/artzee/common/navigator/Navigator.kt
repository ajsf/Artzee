package com.doublea.artzee.common.navigator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.Constants.ART_ID_KEY
import com.doublea.artzee.common.Constants.COLOR_ID_KEY
import com.doublea.artzee.common.extensions.launchFragment

interface Navigator {
    fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int)
}

class NavigatorImpl(private val fragmentActivity: FragmentActivity) : Navigator {

    override fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int) {

        fun launchNewFragment(fragmentManager: FragmentManager) {
            val bundle = Bundle().apply {
                putString(ART_ID_KEY, artId)
                putInt(COLOR_ID_KEY, colorId)
            }

            ArtDetailFragment()
                .apply { arguments = bundle }
                .launchFragment(fragmentManager, true, ArtDetailFragment.TAG, sharedViews)
        }

        with(fragmentActivity.supportFragmentManager) {
            findFragmentByTag(ArtDetailFragment.TAG) ?: launchNewFragment(this)
        }
    }
}
