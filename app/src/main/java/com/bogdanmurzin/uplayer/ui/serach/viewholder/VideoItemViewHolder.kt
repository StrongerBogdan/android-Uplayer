package com.bogdanmurzin.uplayer.ui.serach.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.RecyclerItemListBinding
import com.bumptech.glide.Glide

class VideoItemViewHolder(private val binding: RecyclerItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(videoItem: VideoItem) {
        binding.videoName.text = videoItem.title
        binding.authorName.text = videoItem.author
        Glide.with(binding.root.context)
            .load(videoItem.imageUrl)
            .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
            .centerCrop()
            .into(binding.listImageView)
    }
}