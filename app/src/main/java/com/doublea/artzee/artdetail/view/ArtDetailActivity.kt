package com.doublea.artzee.artdetail.view

import android.os.Bundle
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.di.artDetailModule
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.browse.view.TRANSITION_TIME
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.loadImage
import com.doublea.artzee.common.model.Art
import kotlinx.android.synthetic.main.activity_art_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class ArtDetailActivity : AppCompatActivity(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(artDetailModule())
    }

    private val viewModel: ArtDetailViewModel by buildViewModel()

    private lateinit var artId: String
    private var colorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupUi()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> true
        android.R.id.home -> {
            supportFragmentManager.popBackStack()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupUi() {
        artId = intent.getStringExtra("artId")
        colorId = intent.getIntExtra("colorId", 0)
        observeViewModel()
        setupTransition()
    }

    private fun observeViewModel() {
        viewModel.selectArt(artId)

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

    private fun setArtDetails(art: Art) {
        iv_art.loadImage(art.imageRectangle) {
            (iv_art.parent as ViewGroup).doOnPreDraw {
                startPostponedEnterTransition()
            }
            with(supportFragmentManager) {
                findFragmentByTag(ArtDetailTextFragment.TAG) ?: ArtDetailTextFragment
                    .launch(this, colorId)
            }
        }
    }

    override fun onEnterAnimationComplete() {
        detail_container.setLayerType(View.LAYER_TYPE_NONE, null)
    }

    private fun setupTransition() {
        postponeEnterTransition()
        iv_art.transitionName = artId
        with(window) {
            enterTransition = Fade().apply {
                duration = TRANSITION_TIME / 2
                startDelay = TRANSITION_TIME / 2
            }
            exitTransition = Fade().apply {
                duration = TRANSITION_TIME
            }
            sharedElementEnterTransition.duration = TRANSITION_TIME
            allowEnterTransitionOverlap = true
            allowReturnTransitionOverlap = true
        }
        detail_container.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }
}
