package com.doublea.artzee.commons.navigator

import androidx.fragment.app.FragmentManager
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.toBundle
import com.doublea.artzee.commons.extensions.launchFragment

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
