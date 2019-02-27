package com.doublea.artzee.artdetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.*
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.di.artDetailModule
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewState
import com.doublea.artzee.common.Constants.ART_ID_KEY
import com.doublea.artzee.common.Constants.COLOR_ID_KEY
import com.doublea.artzee.common.Constants.TRANSITION_TIME
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.extensions.loadImage
import kotlinx.android.synthetic.main.fragment_art_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ArtDetailFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    private lateinit var artId: String
    private var colorId: Int = 0

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(artDetailModule())
    }

    private val transition = TransitionSet()
        .addTransition(ChangeBounds())
        .addTransition(ChangeTransform())
        .addTransition(ChangeImageTransform())
        .setDuration(TRANSITION_TIME)

    private val viewModel: ArtDetailViewModel by buildViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artId = arguments?.getString(ART_ID_KEY) ?: ""
        colorId = arguments?.getInt(COLOR_ID_KEY) ?: 0
        viewModel.selectArt(artId)
        return container?.inflate(R.layout.fragment_art_detail)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransition()
        observeViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer<ArtDetailViewState> {
            it?.let { viewState -> render(viewState) }
        })
        btn_set_wallpaper.setOnClickListener { viewModel.setWallpaper() }
    }

    private fun render(viewState: ArtDetailViewState) {
        val imageUrl = viewState.imageUrl ?: ""
        if (imageUrl.isNotEmpty()) {
            iv_art.loadImage(imageUrl) {
                iv_art.doOnPreDraw {
                    startPostponedEnterTransition()
                }
                launchTextFragment()
            }
        }
        if (viewState.settingWallpaper) {
            btn_set_wallpaper?.visibility = View.GONE
            progress_set_wallpaper?.visibility = View.VISIBLE
        } else {
            progress_set_wallpaper?.visibility = View.GONE
            btn_set_wallpaper?.visibility = View.VISIBLE
        }
    }

    private fun setupTransition() {
        postponeEnterTransition()
        sharedElementEnterTransition = transition
        iv_art.transitionName = artId
        enterTransition = Fade().apply { duration = TRANSITION_TIME + 100 }
        returnTransition = Fade().apply { duration = TRANSITION_TIME / 2 }
    }

    private fun launchTextFragment() = fragmentManager?.let {
        it.findFragmentByTag(ArtDetailTextFragment.TAG) ?: ArtDetailTextFragment
            .launch(it, colorId)
    }

    companion object {
        const val TAG = "DETAIL_FRAGMENT"
    }
}

