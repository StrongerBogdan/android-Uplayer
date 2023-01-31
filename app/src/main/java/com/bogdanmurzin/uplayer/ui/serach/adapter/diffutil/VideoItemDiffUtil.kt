package com.bogdanmurzin.uplayer.ui.serach.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.bogdanmurzin.domain.entities.VideoItem

class VideoItemDiffUtil : DiffUtil.ItemCallback<VideoItem>() {
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