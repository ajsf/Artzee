package com.doublea.artzee.browse.view

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.doOnPreDraw
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doublea.artzee.R
import com.doublea.artzee.common.extensions.inflate
import com.doublea.artzee.common.extensions.loadImage
import com.doublea.artzee.common.model.Art
import kotlinx.android.synthetic.main.artwork_list_item.view.*

typealias AdapterClickLister = (String, Int, Int) -> Unit

class ArtworkAdapter(
    private val activity: BrowseActivity,
    private val currentPosition: Int,
    columnCount: Int = 2
) : PagedListAdapter<Art, RecyclerView.ViewHolder>(ArtDiffCallback) {

    private var imageSize: Int = 0

    private val backgroundColors = activity.resources.getIntArray(R.array.background_colors)

    lateinit var clickListener: AdapterClickLister

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        imageSize = (displayMetrics.widthPixels / columnCount)
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = parent.inflate(R.layout.artwork_list_item)
        return ArtworkViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val colorId = backgroundColors[position % backgroundColors.size]
        getItem(position)?.let {
            (holder as ArtworkViewHolder).bind(it, colorId, position)
        }
    }

    override fun getItemId(position: Int): Long = getItem(position)?.id.hashCode().toLong()

    companion object {
        val ArtDiffCallback = object : DiffUtil.ItemCallback<Art>() {
            override fun areItemsTheSame(oldItem: Art, newItem: Art) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Art, newItem: Art) = oldItem == newItem
        }
    }

    inner class ArtworkViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val layoutParams = FrameLayout.LayoutParams(imageSize, imageSize)
            view.iv_artwork_list_thumbnail.layoutParams = layoutParams
        }

        fun bind(art: Art, colorId: Int, position: Int) = with(itemView) {
            doOnPreDraw { activity.startPostponedEnterTransition() }

            iv_artwork_list_thumbnail.setBackgroundColor(colorId)
            iv_artwork_list_thumbnail.transitionName = art.id
            iv_artwork_list_thumbnail.loadImage(art.thumbnail, iv_artwork_progress) {

            }
            setOnClickListener { clickListener(art.id, position, colorId) }
        }
    }
}
