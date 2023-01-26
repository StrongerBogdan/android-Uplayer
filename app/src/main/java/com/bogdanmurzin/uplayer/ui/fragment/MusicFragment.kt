package com.bogdanmurzin.uplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.uplayer.adapter.ChartsRecyclerViewAdapter
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.FragmentMusicBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicFragment : Fragment() {

    private lateinit var binding: FragmentMusicBinding
    private lateinit var recyclerAdapter1: ChartsRecyclerViewAdapter
    private lateinit var recyclerAdapter2: ChartsRecyclerViewAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicBinding.inflate(layoutInflater)
        binding.headerLayout.searchBarInputLayout.setEndIconOnClickListener {
            // TODO Search
        }
        setupViewModel()

        return binding.root
    }

    private fun setupViewModel() {
        viewModel.chartList.observe(viewLifecycleOwner) {
            recyclerAdapter1.submitList(it)
        }

        viewModel.secondChartList.observe(viewLifecycleOwner) {
            recyclerAdapter2.submitList(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirstRecycler()
        setupSecondRecycler()
        viewModel.updateVideoList()
    }

    private fun setupFirstRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerAdapter1 =
            ChartsRecyclerViewAdapter(ChartsRecyclerViewAdapter.FIRST_CHART_VIEW_LAYOUT) {
                viewModel.getVideoIds(recyclerAdapter1.currentList, it)
            }
        val recyclerView = binding.recyclerCharts
        recyclerView.adapter = recyclerAdapter1
        recyclerView.layoutManager = layoutManager
    }

    private fun setupSecondRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), Constants.SPAN_COUNT)
        recyclerAdapter2 =
            ChartsRecyclerViewAdapter(ChartsRecyclerViewAdapter.SECOND_CHART_VIEW_LAYOUT) {
                viewModel.getVideoIds(recyclerAdapter2.currentList, it)
            }
        val recyclerView = binding.recyclerSecondCharts
        recyclerView.adapter = recyclerAdapter2
        recyclerView.layoutManager = layoutManager
    }
}