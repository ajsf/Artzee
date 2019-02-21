package com.doublea.artzee.browse.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.doublea.artzee.R
import com.doublea.artzee.browse.di.browseArtModule
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.model.ArtPagedList
import com.doublea.artzee.common.navigator.Navigator
import kotlinx.android.synthetic.main.fragment_browse_art.*
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

    private lateinit var adapter: ArtworkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_browse_art)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        initAdapter()
        (view.parent as ViewGroup).doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun initRecyclerView() = artwork_list.apply {
        setHasFixedSize(true)
        val gridLayout = GridLayoutManager(context, 2)
        layoutManager = gridLayout
        clearOnScrollListeners()
    }

    private fun initAdapter() {
        if (artwork_list.adapter == null) {
            adapter = ArtworkAdapter(activity, { art, thumbnailView ->
                navigator.viewArtDetail(art.id, thumbnailView)
            })
        }
        artwork_list.adapter = adapter

        viewModel.artList.observe(this,
            Observer<ArtPagedList> {
                adapter.submitList(it.list)
            })
    }
}