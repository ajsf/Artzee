package com.doublea.artzee.browse.view

import android.os.Bundle
import android.transition.Explode
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.doublea.artzee.R
import com.doublea.artzee.browse.di.browseArtModule
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.navigator.Navigator
import kotlinx.android.synthetic.main.activity_browse.*
import org.jetbrains.anko.find
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

const val TRANSITION_TIME = 600L

class BrowseActivity : AppCompatActivity(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(browseArtModule(this@BrowseActivity))
    }

    private val viewModel: BrowseArtViewModel by buildViewModel()

    private val navigator: Navigator by instance()

    private val adapterClickLister: AdapterClickLister = { artId, position, colorId ->
        viewModel.currentPosition = position
        val imageView = getImageViewForPosition(position)
        navigator.viewArtDetail(artId, listOf(imageView), colorId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        setSupportActionBar(toolbar)
        setupUi()
    }

    private fun setupUi() {
        initRecyclerView()
        setupTransitions()
        val adapter = initAdapter(viewModel.currentPosition)
        observeViewModel(adapter)
    }

    private fun initRecyclerView() = artwork_list.apply {
        setHasFixedSize(true)
        val gridLayout = GridLayoutManager(context, 2)
        layoutManager = gridLayout
        clearOnScrollListeners()
    }

    private fun initAdapter(currentPosition: Int): ArtworkAdapter {
        val adapter = ArtworkAdapter(this, currentPosition)
        adapter.clickListener = adapterClickLister
        artwork_list.adapter = adapter
        return adapter
    }

    private fun observeViewModel(adapter: ArtworkAdapter) {
        viewModel.artList.observe(this,
            Observer<ArtPagedList> {
                adapter.submitList(it.list)
            })
    }

    private fun getImageViewForPosition(position: Int): ImageView {
        val holder = artwork_list.findViewHolderForAdapterPosition(position)
                as ArtworkAdapter.ArtworkViewHolder

        return holder.itemView.find(R.id.iv_artwork_list_thumbnail)
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        artwork_list.setLayerType(View.LAYER_TYPE_NONE, null)
    }

    private fun setupTransitions() {
        postponeEnterTransition()
        with(window) {
            exitTransition = Explode().apply {
                duration = TRANSITION_TIME
                startDelay = TRANSITION_TIME / 2
            }
            reenterTransition = Explode().apply {
                duration = TRANSITION_TIME - (TRANSITION_TIME / 4)
                startDelay = TRANSITION_TIME / 4
            }
            allowEnterTransitionOverlap = true
            allowReturnTransitionOverlap = true
        }
        artwork_list.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }
}