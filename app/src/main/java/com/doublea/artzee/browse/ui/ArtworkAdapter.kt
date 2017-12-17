package com.doublea.artzee.browse.ui

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.doublea.artzee.R
import com.doublea.artzee.commons.data.models.Art
import com.doublea.artzee.commons.extensions.inflate
import com.doublea.artzee.commons.extensions.loadImage
import kotlinx.android.synthetic.main.artwork_list_item.view.*

class ArtworkAdapter(val activity: Activity, val clickListener: (Art) -> Unit, columnCount: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataSource: List<Art> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var imageSize: Int = 0

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        imageSize = (displayMetrics.widthPixels / columnCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.artwork_list_item)
        return ArtworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ArtworkViewHolder
        holder.bind(dataSource[position])
    }

    override fun getItemCount() = dataSource.size

    inner class ArtworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val layoutParams = RelativeLayout.LayoutParams(imageSize, imageSize)
            view.iv_artwork_list_thumbnail.layoutParams = layoutParams
        }

        fun bind(art: Art) = with(itemView) {
            iv_artwork_list_thumbnail.loadImage(art.thumbnail)
            setOnClickListener { clickListener(art) }
        }
    }
}