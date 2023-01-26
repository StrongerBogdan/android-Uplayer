package com.bogdanmurzin.uplayer.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.bogdanmurzin.uplayer.common.Constants.SEARCH_DEBOUNCE
import com.bogdanmurzin.uplayer.common.Constants.SEARCH_LENGTH_TRIGGER
import com.bogdanmurzin.uplayer.databinding.FragmentMusicBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

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
        setupSearchEditText()
        setupViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFirstRecycler()
        setupSecondRecycler()
        viewModel.updateVideoList()
    }

    private fun setupSearchEditText() {
        val searchText = binding.headerLayout.searchBarEditText
        val query: Flow<String> = callbackFlow {
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty()) this@callbackFlow.trySend(s.toString()).isSuccess
                }

                override fun beforeTextChanged(s: CharSequence?, st: Int, co: Int, af: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            searchText.addTextChangedListener(textWatcher)
            awaitClose { searchText.removeTextChangedListener(textWatcher) }
        }
            .filter { text -> text.isNotEmpty() && text.length >= SEARCH_LENGTH_TRIGGER }
            .debounce(SEARCH_DEBOUNCE)
            .distinctUntilChanged()
        viewModel.search(query)
    }

    private fun setupViewModel() {
        viewModel.chartList.observe(viewLifecycleOwner) {
            recyclerAdapter1.submitList(it)
        }

        viewModel.secondChartList.observe(viewLifecycleOwner) {
            recyclerAdapter2.submitList(it)
        }
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