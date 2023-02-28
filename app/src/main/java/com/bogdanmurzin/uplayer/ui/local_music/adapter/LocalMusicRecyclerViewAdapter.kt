package com.bogdanmurzin.uplayer.ui.local_music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.uplayer.databinding.RecyclerItemListBinding
import com.bogdanmurzin.uplayer.ui.local_music.adapter.diffutil.LocalMusicDiffUtil
import com.bogdanmurzin.uplayer.ui.local_music.viewholder.LocalMusicViewHolder

class LocalMusicRecyclerViewAdapter(val onItemClicked: (LocalMusic) -> Unit) :
    ListAdapter<LocalMusic, LocalMusicViewHolder>(LocalMusicDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocalMusicViewHolder =
        LocalMusicViewHolder(
            RecyclerItemListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: LocalMusicViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }
}