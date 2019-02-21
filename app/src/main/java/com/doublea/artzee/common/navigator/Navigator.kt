package com.doublea.artzee.common.navigator

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.doublea.artzee.artdetail.view.ART_ID_KEY
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.extensions.launchFragment

interface Navigator {
    fun viewArtDetail(artId: String, thumbnailView: ImageView)
}

class NavigatorImpl(private val fragmentActivity: FragmentActivity) : Navigator {

    override fun viewArtDetail(artId: String, thumbnailView: ImageView) {
        ArtDetailFragment().apply {
            arguments = Bundle().also {
                it.putString(ART_ID_KEY, artId)
            }
            launchFragment(
                fragmentActivity.supportFragmentManager,
                sharedElements = listOf(thumbnailView)
            )
        }
    }
}
