package com.doublea.artzee.browse.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.doublea.artzee.R
import com.doublea.artzee.browse.di.browseArtModule
import com.doublea.artzee.browse.viewmodel.BrowseArtViewModel
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.extensions.buildViewModel
import com.doublea.artzee.commons.extensions.inflate
import com.doublea.artzee.commons.navigator.NavigatorImpl
import kotlinx.android.synthetic.main.fragment_browse_art.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class BrowseArtFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(browseArtModule())
    }

    private val viewModel: BrowseArtViewModel by buildViewModel()

    private lateinit var adapter: ArtworkAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Browse", "onAttach")
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_browse_art)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initRecyclerView()
        initAdapter()
    }

    private fun initViewModel() {
        fragmentManager?.let {
            viewModel.navigator = NavigatorImpl(it)
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
            adapter = ArtworkAdapter(activity, { art ->
                viewModel.selectArtItem(art)
            })
        }
        artwork_list.adapter = adapter
        viewModel.artList.observe(this, Observer<PagedList<Art>> { adapter.submitList(it) })
    }
}