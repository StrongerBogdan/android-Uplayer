package com.bogdanmurzin.uplayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.adapter.ChartsRecyclerViewAdapter
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.FragmentMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicFragment : Fragment() {

    private lateinit var binding: FragmentMusicBinding
    private lateinit var recyclerAdapter: ChartsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(layoutInflater)
        setupFirstRecycler()
        setupSecondRecycler()

        return binding.root
    }

    private fun setupFirstRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        recyclerAdapter =
            ChartsRecyclerViewAdapter(ChartsRecyclerViewAdapter.FIRST_CHART_VIEW_LAYOUT) {
                // start video in viewModel
                Log.i(Constants.TAG, "Video Start")
            }
        val recyclerView = binding.recyclerCharts
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
        recyclerAdapter.submitList(createTempList())
    }

    private fun setupSecondRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), Constants.SPAN_COUNT)
        recyclerAdapter =
            ChartsRecyclerViewAdapter(ChartsRecyclerViewAdapter.SECOND_CHART_VIEW_LAYOUT) {
                // start video in viewModel
                Log.i(Constants.TAG, "Video Start ${it.videoId}")
            }
        val recyclerView = binding.recyclerSecondCharts
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
        recyclerAdapter.submitList(createTempList())
    }

    // DELETE placeholder
    private fun createTempList() =
        listOf(
            VideoItem(
                0,
                "Imagine Dragons - Bones (Official Music Video)",
                "ImagineDragonsVEVO",
                "https://i.ytimg.com/vi/TO-_3tck2tg/mqdefault.jpg",
                "TO-_3tck2tg"
            ),
            VideoItem(
                1,
                "Sharks",
                "ImagineDragonsVEVO",
                "https://i.ytimg.com/vi/Te3_VlimRw0/mqdefault.jpg",
                "Te3_VlimRw0"
            ),
            VideoItem(
                2,
                "Bones",
                "ImagineDragonsVEVO",
                "https://i.ytimg.com/vi/DYed5whEf4g/mqdefault.jpg",
                "DYed5whEf4g"
            ),
            VideoItem(
                3,
                "Born For This",
                "TheScoreVEVO",
                "https://i.ytimg.com/vi/aJ5IzGBnWAc/mqdefault.jpg",
                "aJ5IzGBnWAc"
            ),
            VideoItem(
                4,
                "Stronger",
                "TheScoreVEVO",
                "https://i.ytimg.com/vi/cNld-AHw-Wg/mqdefault.jpg",
                "cNld-AHw-Wg"
            ),
            VideoItem(
                5,
                "Unstoppable",
                "TheScoreVEVO",
                "https://i.ytimg.com/vi/_PBlykN4KIY/mqdefault.jpg",
                "_PBlykN4KIY"
            )
        )
}