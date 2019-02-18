package com.doublea.artzee.common.navigator

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.doublea.artzee.artdetail.view.ArtDetailFragment
import com.doublea.artzee.common.extensions.launchFragment
import com.doublea.artzee.common.model.Art

interface Navigator {
    fun viewArtDetail(art: Art)
}

class NavigatorImpl(private val fm: FragmentManager) : Navigator {

    override fun viewArtDetail(art: Art) {
        ArtDetailFragment().apply {
            arguments = Bundle()
                .also { it.putString("artId", art.id) }

            launchFragment(fm)
        }
    }
}
