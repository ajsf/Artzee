package com.doublea.artzee.artdetail.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.utils.setWallpaper
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.data.models.Artist
import com.doublea.artzee.commons.extensions.inflate
import com.doublea.artzee.commons.extensions.loadImage
import kotlinx.android.synthetic.main.fragment_art_detail.*

class ArtDetailFragment : Fragment() {

    private lateinit var viewModel: ArtDetailViewModel

    private lateinit var art: Art

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        art = this.arguments?.getParcelable("art") as Art
        viewModel = ArtDetailViewModel.createViewModel(this, art)

        return container?.inflate(R.layout.fragment_art_detail)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_set_wallpaper.setOnClickListener { setWallpaper() }
        observeViewModel()
    }

    private fun setWallpaper() {
        val imageUrl = art.getImageUrl("larger")
        setWallpaper(this.context, imageUrl)
    }

    private fun observeViewModel() {
        viewModel.artLiveData.observe(this, Observer<Art> {
            it?.let {
                if (it != art) art = it
                setArtDetails()
            }
        })

        viewModel.artistLiveData.observe(this, Observer<Artist> {
            tv_artists.setTextAndVisibility(it?.name ?: "")
        })
    }

    private fun setArtDetails() {
        iv_art.loadImage(art.getImageUrl("large_rectangle"), art_detail_progress)
        val details = "${art.medium}, ${art.date}"
        tv_title.text = art.title
        tv_details.text = details
        tv_details2.setTextAndVisibility(
                if (art.collectingInstitution.isBlank()) {
                    ""
                } else {
                    "Collecting institution: ${art.collectingInstitution}"
                })
    }

    private fun TextView.setTextAndVisibility(value: String) {
        if (value.isBlank()) {
            visibility = View.GONE
        } else {
            text = value
            visibility = View.VISIBLE
        }
    }
}

