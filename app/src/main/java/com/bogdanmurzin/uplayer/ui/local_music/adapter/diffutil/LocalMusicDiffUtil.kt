package com.bogdanmurzin.uplayer.ui.local_music.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.bogdanmurzin.domain.entities.LocalMusic

class LocalMusicDiffUtil : DiffUtil.ItemCallback<LocalMusic>() {
    override fun areItemsTheSame(oldItem: LocalMusic, newItem: LocalMusic): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: LocalMusic,
        newItem: LocalMusic
    ): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.author == newItem.author &&
                oldItem.id == newItem.id &&
                oldItem.coverArtUri == newItem.coverArtUri
    }
}