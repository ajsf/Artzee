package com.doublea.artzee.browse.view

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doublea.artzee.R
import com.doublea.artzee.common.model.Art
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.extensions.loadImage
import kotlinx.android.synthetic.main.artwork_list_item.view.*

class ArtworkAdapter(activity: Activity?, val clickListener: (Art) -> Unit, columnCount: Int = 2) : PagedListAdapter<Art, RecyclerView.ViewHolder>(ArtDiffCallback) {

    private var imageSize: Int = 0

    init {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        imageSize = (displayMetrics.widthPixels / columnCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.artwork_list_item)
        return ArtworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ArtworkViewHolder
        holder.bind(getItem(position))
    }

    companion object {
        val ArtDiffCallback = object : DiffUtil.ItemCallback<Art>() {
            override fun areItemsTheSame(oldItem: Art, newItem: Art) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Art, newItem: Art) = oldItem == newItem
        }
    }

    inner class ArtworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val layoutParams = RelativeLayout.LayoutParams(imageSize, imageSize)
            view.iv_artwork_list_thumbnail.layoutParams = layoutParams
        }

        fun bind(art: Art?) = with(itemView) {
            art?.let { a ->
                iv_artwork_list_thumbnail.loadImage(a.thumbnail, iv_artwork_progress)
                setOnClickListener { clickListener(a) }
                ViewCompat.setTransitionName(this, art.id)
            }
        }
    }
}