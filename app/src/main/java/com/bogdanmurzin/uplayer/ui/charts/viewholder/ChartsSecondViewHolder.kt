package com.bogdanmurzin.uplayer.ui.charts.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.RecyclerItemSecondChartsBinding
import com.bumptech.glide.Glide

class ChartsSecondViewHolder(private val binding: RecyclerItemSecondChartsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(videoItem: VideoItem) {
        binding.secondChartVideoName.text = videoItem.title
        binding.secondChartAuthor.text = videoItem.author.uppercase()
        Glide.with(binding.root.context)
            .load(videoItem.coverArtUri)
            .override(Constants.SECOND_CHARTS_IMG_WIDTH, Constants.SECOND_CHARTS_IMG_HEIGHT)
            .centerCrop()
            .into(binding.secondChartImageView)
    }
}