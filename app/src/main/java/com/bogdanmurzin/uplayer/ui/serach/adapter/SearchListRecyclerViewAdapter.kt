package com.bogdanmurzin.uplayer.ui.serach.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.databinding.RecyclerItemListBinding
import com.bogdanmurzin.uplayer.ui.serach.adapter.diffutil.VideoItemDiffUtil
import com.bogdanmurzin.uplayer.ui.serach.viewholder.VideoItemViewHolder

class SearchListRecyclerViewAdapter(val onItemClicked: (VideoItem) -> Unit) :
    ListAdapter<VideoItem, VideoItemViewHolder>(VideoItemDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoItemViewHolder =
        VideoItemViewHolder(
            RecyclerItemListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }
}