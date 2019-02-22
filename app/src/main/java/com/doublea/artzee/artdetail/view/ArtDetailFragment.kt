package com.doublea.artzee.artdetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.transition.*
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.di.artDetailModule
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.extensions.launchFragment
import com.doublea.artzee.common.extensions.loadImage
import com.doublea.artzee.common.model.Art
import kotlinx.android.synthetic.main.fragment_art_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

private const val ART_ID_KEY = "ART_ID"

const val TRANSITION_TIME = 700L

class ArtDetailFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    private lateinit var artId: String

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
        viewModel.artLiveData.observe(this, Observer<Art> {
            it?.let { art -> setArtDetails(art) }
        })

        viewModel.settingWallpaper.observe(this, Observer<Boolean> {
            if (it) {
                btn_set_wallpaper.visibility = View.GONE
                progress_set_wallpaper.visibility = View.VISIBLE
            } else {
                progress_set_wallpaper?.visibility = View.GONE
                btn_set_wallpaper?.visibility = View.VISIBLE
            }
        })

        btn_set_wallpaper.setOnClickListener { viewModel.setWallpaper() }
    }

    private fun setupTransition() {
        postponeEnterTransition()
        sharedElementEnterTransition = transition
        iv_art.transitionName = artId
        enterTransition = Fade().apply { duration = TRANSITION_TIME + 100 }
        returnTransition = Fade().apply { duration = TRANSITION_TIME / 2 }
    }

    private fun setArtDetails(art: Art) {
        iv_art.loadImage(art.imageRectangle) {
            startPostponedEnterTransition()
        }
    }

    companion object {
        fun launch(fragmentManager: FragmentManager, artId: String, sharedViews: List<View>) {
            val bundle = Bundle().apply { putString(ART_ID_KEY, artId) }

            ArtDetailFragment()
                .apply { arguments = bundle }
                .launchFragment(fragmentManager, true, null, sharedViews)
        }
    }
}

