package com.bogdanmurzin.uplayer.ui.local_music.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.common.GlideApp
import com.bogdanmurzin.uplayer.databinding.RecyclerItemListBinding

class LocalMusicViewHolder(private val binding: RecyclerItemListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(localMusic: LocalMusic) {
        binding.videoName.text = localMusic.title
        binding.authorName.text = localMusic.author
        GlideApp.with(binding.root.context)
            .load(localMusic.coverArtUri)
            .error(R.drawable.no_item_icon)
            .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
            .centerCrop()
            .into(binding.listImageView)
    }
}