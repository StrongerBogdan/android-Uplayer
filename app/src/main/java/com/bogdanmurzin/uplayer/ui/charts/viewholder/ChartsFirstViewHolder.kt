package com.bogdanmurzin.uplayer.ui.charts.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.RecyclerItemChartsBinding
import com.bumptech.glide.Glide

class ChartsFirstViewHolder(private val binding: RecyclerItemChartsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(videoItem: VideoItem) {
        binding.chartVideoName.text = videoItem.title
        binding.chartAuthorName.text = videoItem.author.uppercase()
        Glide.with(binding.root.context)
            .load(videoItem.imageUrl)
            .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
            .centerCrop()
            .into(binding.chartImageView)
    }
}