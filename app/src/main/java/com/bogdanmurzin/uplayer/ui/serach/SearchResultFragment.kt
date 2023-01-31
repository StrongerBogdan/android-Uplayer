package com.bogdanmurzin.uplayer.ui.serach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.databinding.FragmnetSearchBinding
import com.bogdanmurzin.uplayer.ui.serach.adapter.SearchListRecyclerViewAdapter
import com.bogdanmurzin.uplayer.util.extension.RecyclerViewExtension.setDivider
import com.bogdanmurzin.uplayer.viewmodel.MainViewModel
import com.bogdanmurzin.uplayer.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmnetSearchBinding
    private lateinit var recyclerAdapter: SearchListRecyclerViewAdapter
    private val viewModel: SearchViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmnetSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        viewModel.videoList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
        val args: SearchResultFragmentArgs by navArgs()
        viewModel.search(args.query)
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = SearchListRecyclerViewAdapter {
            mainViewModel.getVideoIds(recyclerAdapter.currentList, it)
        }
        val recyclerView = binding.searchRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setDivider(R.drawable.line_divider)
    }
}