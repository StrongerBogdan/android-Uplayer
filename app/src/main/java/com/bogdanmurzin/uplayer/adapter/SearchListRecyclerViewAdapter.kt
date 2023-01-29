package com.bogdanmurzin.uplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_IMG_HEIGHT
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_IMG_WIDTH
import com.bogdanmurzin.uplayer.databinding.RecyclerItemListBinding
import com.bumptech.glide.Glide

class SearchListRecyclerViewAdapter(val onItemClicked: (VideoItem) -> Unit) :
    ListAdapter<VideoItem, SearchListRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListRecyclerViewAdapter.ViewHolder =
        ViewHolder(
            RecyclerItemListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    inner class ViewHolder(private val binding: RecyclerItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) {
            binding.videoName.text = videoItem.title
            binding.authorName.text = videoItem.author
            Glide.with(binding.root.context)
                .load(videoItem.imageUrl)
                .override(CHARTS_IMG_WIDTH, CHARTS_IMG_HEIGHT)
                .centerCrop()
                .into(binding.listImageView)
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
}