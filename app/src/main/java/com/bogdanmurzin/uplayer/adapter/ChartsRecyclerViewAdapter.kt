package com.bogdanmurzin.uplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_IMG_HEIGHT
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_IMG_WIDTH
import com.bogdanmurzin.uplayer.common.Constants.SECOND_CHARTS_IMG_HEIGHT
import com.bogdanmurzin.uplayer.common.Constants.SECOND_CHARTS_IMG_WIDTH
import com.bogdanmurzin.uplayer.databinding.RecyclerItemChartsBinding
import com.bogdanmurzin.uplayer.databinding.RecyclerItemSecondChartsBinding
import com.bumptech.glide.Glide

class ChartsRecyclerViewAdapter(
    private val currentType: Int,
    val onItemClicked: (VideoItem) -> Unit
) :
    ListAdapter<VideoItem, RecyclerView.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FIRST_CHART_VIEW_LAYOUT ->
                Charts1ViewHolder(
                    RecyclerItemChartsBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            SECOND_CHART_VIEW_LAYOUT ->
                Charts2ViewHolder(
                    RecyclerItemSecondChartsBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            FIRST_CHART_VIEW_LAYOUT ->
                (holder as Charts1ViewHolder).bind(getItem(position))
            SECOND_CHART_VIEW_LAYOUT ->
                (holder as Charts2ViewHolder).bind(getItem(position))
        }
        val item = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    override fun getItemViewType(position: Int): Int {
        return currentType
    }

    inner class Charts1ViewHolder(private val binding: RecyclerItemChartsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            binding.chartVideoName.text = videoItem.title
            binding.chartAuthorName.text = videoItem.author
            Glide.with(binding.root.context)
                .load(videoItem.imageUrl)
                .override(CHARTS_IMG_WIDTH, CHARTS_IMG_HEIGHT)
                .centerCrop()
                .into(binding.chartImageView)
        }
    }

    inner class Charts2ViewHolder(private val binding: RecyclerItemSecondChartsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            binding.secondChartVideoName.text = videoItem.title
            binding.secondChartAuthor.text = videoItem.author
            Glide.with(binding.root.context)
                .load(videoItem.imageUrl)
                .override(SECOND_CHARTS_IMG_WIDTH, SECOND_CHARTS_IMG_HEIGHT)
                .centerCrop()
                .into(binding.secondChartImageView)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<VideoItem>() {
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: VideoItem,
            newItem: VideoItem
        ): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.author == newItem.author &&
                    oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.videoId == newItem.videoId
        }
    }

    companion object {
        const val FIRST_CHART_VIEW_LAYOUT = 1
        const val SECOND_CHART_VIEW_LAYOUT = 2
    }
}