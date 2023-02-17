package com.bogdanmurzin.uplayer.ui.charts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.databinding.RecyclerItemChartsBinding
import com.bogdanmurzin.uplayer.databinding.RecyclerItemSecondChartsBinding
import com.bogdanmurzin.uplayer.ui.charts.adapter.diffutil.VideoItemDiffUtil
import com.bogdanmurzin.uplayer.ui.charts.viewholder.ChartsFirstViewHolder
import com.bogdanmurzin.uplayer.ui.charts.viewholder.ChartsSecondViewHolder

class ChartsRecyclerViewAdapter(
    private val currentType: Int,
    val onItemClicked: (VideoItem) -> Unit
) : ListAdapter<VideoItem, RecyclerView.ViewHolder>(VideoItemDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FIRST_CHART_VIEW_LAYOUT ->
                ChartsFirstViewHolder(
                    RecyclerItemChartsBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            SECOND_CHART_VIEW_LAYOUT ->
                ChartsSecondViewHolder(
                    RecyclerItemSecondChartsBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            FIRST_CHART_VIEW_LAYOUT ->
                (holder as ChartsFirstViewHolder).bind(getItem(position))
            SECOND_CHART_VIEW_LAYOUT ->
                (holder as ChartsSecondViewHolder).bind(getItem(position))
        }
        val item = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    override fun getItemViewType(position: Int): Int {
        return currentType
    }

    companion object {
        const val FIRST_CHART_VIEW_LAYOUT = 1
        const val SECOND_CHART_VIEW_LAYOUT = 2
    }
}