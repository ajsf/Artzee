package com.doublea.artzee.browse.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.Explode
import com.doublea.artzee.R
import com.doublea.artzee.browse.di.browseArtModule
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.common.Constants.TRANSITION_TIME
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.navigator.Navigator
import kotlinx.android.synthetic.main.fragment_browse_art.*
import org.jetbrains.anko.find
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class BrowseArtFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(browseArtModule())
    }

    private val viewModel: BrowseArtViewModel by buildViewModel()

    private val navigator: Navigator by instance()

    private val adapterClickLister: AdapterClickLister = { artId, position, colorId ->
        viewModel.currentPosition = position
        if (position == 0) artwork_list.fling(0, 200)
        val imageView = getImageViewForPosition(position)
        navigator.viewArtDetail(artId, listOf(imageView), colorId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupTransitions()
        return container?.inflate(R.layout.fragment_browse_art)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = initAdapter(viewModel.currentPosition)
        observeViewModel(adapter)
    }

    private fun setupTransitions() {
        postponeEnterTransition()
        exitTransition = Explode().apply { duration = TRANSITION_TIME / 2 }
        allowEnterTransitionOverlap = true
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
        if (currentPosition == 0) artwork_list.fling(0, -500)
        return adapter
    }

    private fun observeViewModel(adapter: ArtworkAdapter) {
        viewModel.artList.observe(activity as LifecycleOwner,
            Observer<ArtPagedList> {
                adapter.submitList(it.list)
            })
    }

    private fun getImageViewForPosition(position: Int): ImageView {
        val holder = artwork_list.findViewHolderForAdapterPosition(position)
                as ArtworkAdapter.ArtworkViewHolder

        return holder.itemView.find(R.id.iv_artwork_list_thumbnail)
    }

    companion object {
        const val TAG = "BROWSE_FRAGMENT"
    }
}