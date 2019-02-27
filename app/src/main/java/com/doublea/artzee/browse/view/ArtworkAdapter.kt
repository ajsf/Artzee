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
import org.jetbrains.anko.backgroundColor

typealias AdapterClickLister = (String, Int, Int) -> Unit

class ArtworkAdapter(
    private val fragment: BrowseArtFragment,
    private val currentPosition: Int,
    private val columnCount: Int
) : PagedListAdapter<Art, RecyclerView.ViewHolder>(ArtDiffCallback) {

    private var imageSize: Int = 0

    private val backgroundColors = fragment.resources.getIntArray(R.array.background_colors)

    lateinit var clickListener: AdapterClickLister

    init {
        setImageSize()
        setHasStableIds(true)
    }

    private fun setImageSize() {
        val displayMetrics = DisplayMetrics()
        fragment.activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        imageSize = (displayMetrics.widthPixels) / columnCount
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
            iv_artwork_list_thumbnail.backgroundColor = colorId
            iv_artwork_list_thumbnail.transitionName = art.id
            iv_artwork_list_thumbnail.loadImage(art.thumbnail, iv_artwork_progress) {
                if (position == currentPosition) {
                    doOnPreDraw { fragment.startPostponedEnterTransition() }
                }
            }
            setOnClickListener { clickListener(art.id, position, colorId) }
        }
    }
}
