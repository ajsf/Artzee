package com.doublea.artzee.browse.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublea.artzee.R
import com.doublea.artzee.commons.extensions.inflate
import kotlinx.android.synthetic.main.fragment_browse_art.*

class BrowseArtFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: ArtworkAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_browse_art)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        artwork_list.apply {
            setHasFixedSize(true)
            val gridLayout = GridLayoutManager(context, 2)
            layoutManager = gridLayout
            clearOnScrollListeners()
        }

        initAdapter()
        viewModel.art.observe(this, Observer { if (it != null) adapter.dataSource = it })
    }

    private fun initAdapter() {
        if (artwork_list.adapter == null) {
            adapter = ArtworkAdapter(this.activity, {}, 2)
        }
        artwork_list.adapter = adapter
    }
}