package com.doublea.artzee.common.navigator

import androidx.fragment.app.FragmentManager
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.extensions.launchFragment
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.toBundle

interface Navigator {
    fun viewArtDetail(art: Art)
}

class NavigatorImpl(private val fm: FragmentManager) : Navigator {

    override fun viewArtDetail(art: Art) {
        ArtDetailFragment().apply {
            arguments = art.toBundle()
            launchFragment(fm)
        }
    }
}
