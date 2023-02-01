package com.bogdanmurzin.uplayer.ui.charts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.FragmentMusicBinding
import com.bogdanmurzin.uplayer.model.event.Event
import com.bogdanmurzin.uplayer.ui.MainActivity
import com.bogdanmurzin.uplayer.ui.charts.adapter.ChartsRecyclerViewAdapter
import com.bogdanmurzin.uplayer.util.extension.Extensions.hideKeyboard
import com.bogdanmurzin.uplayer.viewmodel.charts.ChartsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicFragment : Fragment(R.layout.fragment_music) {

    private val binding: FragmentMusicBinding by viewBinding(FragmentMusicBinding::bind)
    private lateinit var recyclerAdapter1: ChartsRecyclerViewAdapter
    private lateinit var recyclerAdapter2: ChartsRecyclerViewAdapter
    private val viewModel by viewModels<ChartsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupFirstRecycler()
        setupSecondRecycler()
        viewModel.fetchCharts()
        (activity as AppCompatActivity).setSupportActionBar(binding.myToolbar)

        with(binding.headerFragment) {
            searchBarInputLayout.setEndIconOnClickListener {
                // hide keyboard
                this@MusicFragment.hideKeyboard()
                val searchText = searchBarEditText.text
                if (!searchText.isNullOrEmpty()) viewModel.openSearchFragment(searchText.toString())
            }
        }
    }

    private fun setupViewModel() {
        viewModel.chartList.observe(viewLifecycleOwner) { recyclerAdapter1.submitList(it) }
        viewModel.secondChartList.observe(viewLifecycleOwner) { recyclerAdapter2.submitList(it) }
        viewModel.action.observe(viewLifecycleOwner) { event ->
            if (event is Event.OpenSearchFragment) {
                findNavController()
                    .navigate(
                        MusicFragmentDirections.actionMusicFragmentToSearchResultFragment(event.query)
                    )
                // clear searchBar after navigation
                binding.headerFragment.searchBarEditText.text?.clear()
            }
        }
    }

    private fun setupFirstRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerAdapter1 =
            ChartsRecyclerViewAdapter(ChartsRecyclerViewAdapter.FIRST_CHART_VIEW_LAYOUT) {
                // TODO On chart 1 video clicked
                (activity as MainActivity).loadVideoCover(it)
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
                // TODO On chart 1 video clicked
                (activity as MainActivity).loadVideoCover(it)
            }
        val recyclerView = binding.recyclerSecondCharts
        recyclerView.adapter = recyclerAdapter2
        recyclerView.layoutManager = layoutManager
    }
}