package com.doublea.artzee.common.navigator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.extensions.launchFragment

interface Navigator {
    fun viewArtDetail(artId: String)
}

class NavigatorImpl : Navigator {

    override fun viewArtDetail(artId: String) {
        currentActivity?.supportFragmentManager?.let { fm ->
            ArtDetailFragment().apply {
                arguments = Bundle().also { it.putString("artId", artId) }
                launchFragment(fm)
            }
        }
    }

    companion object {
        var currentActivity: FragmentActivity? = null
    }
}
