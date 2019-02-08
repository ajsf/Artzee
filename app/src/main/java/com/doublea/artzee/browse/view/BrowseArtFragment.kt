package com.doublea.artzee.browse.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.doublea.artzee.R
import com.doublea.artzee.browse.viewmodel.MainActivityViewModel
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.extensions.inflate
import com.doublea.artzee.commons.navigator.NavigatorImpl
import kotlinx.android.synthetic.main.fragment_browse_art.*

class BrowseArtFragment : androidx.fragment.app.Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: ArtworkAdapter

    private val artListObserver = Observer<PagedList<Art>> { adapter.submitList(it) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        fragmentManager?.let {
            viewModel.navigator = NavigatorImpl(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_browse_art)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initAdapter()
    }

    private fun initRecyclerView() = artwork_list.apply {
        setHasFixedSize(true)
        val gridLayout = GridLayoutManager(context, 2)
        layoutManager = gridLayout
        clearOnScrollListeners()
    }

    private fun initAdapter() {
        if (artwork_list.adapter == null) {
            adapter = ArtworkAdapter(activity, { art ->
                viewModel.selectArtItem(art)
            })
        }
        artwork_list.adapter = adapter
        viewModel.artList.observe(this, artListObserver)
    }
}