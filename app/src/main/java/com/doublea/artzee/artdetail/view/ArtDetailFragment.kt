package com.doublea.artzee.artdetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.doublea.artzee.R
import com.doublea.artzee.artdetail.di.artDetailModule
import com.doublea.artzee.artdetail.utils.setWallpaper
import com.doublea.artzee.artdetail.viewmodel.ArtDetailViewModel
import com.doublea.artzee.common.extensions.buildViewModel
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.extensions.loadImage
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.model.Artist
import kotlinx.android.synthetic.main.fragment_art_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ArtDetailFragment : Fragment(), KodeinAware {

    private val _parentKodein: Kodein by closestKodein()

    override val kodein = Kodein.lazy {
        extend(_parentKodein)
        import(artDetailModule())
    }

    private val viewModel: ArtDetailViewModel by buildViewModel()

    private lateinit var artId: String

    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artId = this.arguments?.getString("artId") ?: ""
        viewModel.selectArt(artId)
        return container?.inflate(R.layout.fragment_art_detail)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_set_wallpaper.setOnClickListener { setWallpaper() }
        observeViewModel()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setWallpaper() {
        btn_set_wallpaper.visibility = View.GONE
        progress_set_wallpaper.visibility = View.VISIBLE
        val callback = {
            progress_set_wallpaper?.visibility = View.GONE
            btn_set_wallpaper?.visibility = View.VISIBLE
        }
        setWallpaper(this.context, imageUrl, callback)
    }

    private fun observeViewModel() {
        viewModel.artLiveData.observe(this, Observer<Art> {
            it?.let { art ->
                setArtDetails(art)
                imageUrl = art.getImageUrl("larger")
            }
        })

        viewModel.artistLiveData.observe(this, Observer<Artist> {
            tv_artists.setTextAndVisibility(it?.name ?: "")
        })
    }

    private fun setArtDetails(art: Art) {
        iv_art.loadImage(art.getImageUrl("large_rectangle"), art_detail_progress)
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
            text = value
            visibility = View.VISIBLE
        }
    }
}

