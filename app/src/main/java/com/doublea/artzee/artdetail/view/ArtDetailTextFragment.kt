package com.doublea.artzee.artdetail.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import kotlinx.android.synthetic.main.fragment_art_detail_text.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

private const val COLOR_ID_KEY = "COLOR_ID"

class ArtDetailTextFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel: ArtDetailViewModel by buildViewModel()

    private var rotated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_art_detail_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorId = arguments?.getInt(COLOR_ID_KEY) ?: R.color.primaryDarkColor
        card_view.setBackgroundColor(colorId)
        if (savedInstanceState == null) {
            card_view.translationY = -1000f
        } else {
            rotated = true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.artLiveData.observe(this, Observer<Art> {
            it?.let { art -> setArtDetails(art) }
        })

        viewModel.artistLiveData.observe(this, Observer<Artist> {
            tv_artist.setTextAndVisibility(it?.name ?: "")
        })
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return if (enter.not() && nextAnim != 0) {
            val animationListener =
                AnimationListener(endAction = {
                    if (rotated) activity?.finish() else activity?.finishAfterTransition()
                })
            AnimatorInflater.loadAnimator(activity, nextAnim)
                .apply { addListener(animationListener) }
        } else super.onCreateAnimator(transit, enter, nextAnim)
    }

    private fun setArtDetails(art: Art) {
        val details = "${art.medium}, ${art.date}"
        tv_title.text = art.title
        tv_details.text = details
        tv_details2.setTextAndVisibility(
            if (art.collectingInstitution.isBlank()) {
                ""
            } else {
                "Collecting institution: ${art.collectingInstitution}"
            }
        )
    }

    private fun TextView.setTextAndVisibility(value: String) {
        if (value.isBlank()) {
            visibility = View.GONE
        } else {
            text = value.trim()
            visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "TEXT_FRAGMENT"
        fun launch(fragmentManager: FragmentManager, colorId: Int) {
            val bundle = Bundle().also { it.putInt(COLOR_ID_KEY, colorId) }
            val fragment = ArtDetailTextFragment().apply { arguments = bundle }

            fragmentManager.beginTransaction().apply {
                setCustomAnimations(R.animator.slide_in, 0, 0, R.animator.slide_out)
                addToBackStack(null)
                replace(R.id.text_fragment_container, fragment, TAG)
                commit()
            }
        }
    }
}