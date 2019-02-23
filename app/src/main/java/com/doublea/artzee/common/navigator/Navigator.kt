package com.doublea.artzee.common.navigator

import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import com.doublea.artzee.artdetail.view.ArtDetailActivity

interface Navigator {
    fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int)
}

class NavigatorImpl(private val fragmentActivity: FragmentActivity) : Navigator {

    override fun viewArtDetail(artId: String, sharedViews: List<View>, colorId: Int) {
        val options = ActivityOptionsCompat
            .makeSceneTransitionAnimation(fragmentActivity, sharedViews.first(), artId)

        val intent = Intent(fragmentActivity, ArtDetailActivity::class.java).apply {
            putExtra("artId", artId)
            putExtra("colorId", colorId)
        }

        fragmentActivity.startActivity(intent, options.toBundle())
    }
}
