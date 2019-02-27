package com.doublea.artzee.artdetail.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.di.artDetailModule
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewState
import com.doublea.artzee.common.Constants.COLOR_ID_KEY
import com.doublea.artzee.common.extensions.buildViewModel
import kotlinx.android.synthetic.main.fragment_art_detail_text.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ArtDetailTextFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(artDetailModule())
    }

    private val viewModel: ArtDetailViewModel by buildViewModel()

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
            card_view.translationY = -1200f
        }
        observeViewModel()
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return if (enter.not() && activity != null) {
            AnimatorInflater.loadAnimator(activity, nextAnim).apply {
                addListener(AnimationEndListener {
                    (activity as FragmentActivity).supportFragmentManager.popBackStack()
                })
            }
        } else super.onCreateAnimator(transit, enter, nextAnim)
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, Observer<ArtDetailViewState> {
            it?.let { viewState -> render(viewState) }
        })
    }

    private fun render(viewState: ArtDetailViewState) {
        tv_title.text = viewState.title
        tv_details.text = viewState.details
        tv_artist.setTextAndVisibility(viewState.artistName)
        tv_details2.setTextAndVisibility(viewState.details2)
    }

    private fun TextView.setTextAndVisibility(value: String?) {
        if (value.isNullOrBlank()) {
            visibility = View.GONE
        } else {
            text = value.trim()
            visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "DETAIL_TEXT_FRAGMENT"

        fun launch(fragmentManager: FragmentManager, colorId: Int) {
            val bundle = Bundle().also { it.putInt(COLOR_ID_KEY, colorId) }

            val fragment = ArtDetailTextFragment()
                .apply { arguments = bundle }

            fragmentManager.beginTransaction().apply {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.animator.slide_in,
                    R.animator.slide_out,
                    R.animator.slide_in,
                    R.animator.slide_out
                )
                addToBackStack(null)
                replace(R.id.text_fragment_container, fragment, TAG)
                commit()
            }
        }
    }
}

