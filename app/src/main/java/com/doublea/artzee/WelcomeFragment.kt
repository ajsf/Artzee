package com.doublea.artzee

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.doublea.artzee.browse.di.browseArtModule
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class WelcomeFragment : DialogFragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(browseArtModule())
    }

    private val viewModel: BrowseArtViewModel by buildViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        allowEnterTransitionOverlap = true
        allowReturnTransitionOverlap = true
        observeViewModel()
        return inflater.inflate(R.layout.fragment_welcome, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun observeViewModel() {
        viewModel.artList.observe(this, Observer {
            if (it.list.size > 0) {
                Handler().postDelayed({
                    dismiss()
                }, 1200)
            }
        })
    }
}