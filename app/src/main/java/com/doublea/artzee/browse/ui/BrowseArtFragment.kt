package com.doublea.artzee.browse.ui

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublea.artzee.R

/**
 * A placeholder fragment containing a simple view.
 */
class BrowseArtActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_art, container, false)
    }
}
