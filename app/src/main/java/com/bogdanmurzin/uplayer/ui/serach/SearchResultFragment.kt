package com.bogdanmurzin.uplayer.ui.serach

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.databinding.FragmentSearchBinding
import com.bogdanmurzin.uplayer.model.PlayerType
import com.bogdanmurzin.uplayer.ui.MainActivity
import com.bogdanmurzin.uplayer.ui.serach.adapter.SearchListRecyclerViewAdapter
import com.bogdanmurzin.uplayer.util.extension.RecyclerViewExtension.setDivider
import com.bogdanmurzin.uplayer.viewmodel.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var recyclerAdapter: SearchListRecyclerViewAdapter
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        viewModel.videoList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }
        val args: SearchResultFragmentArgs by navArgs()
        binding.headerFragment.searchBarEditText.setText(args.query)
        viewModel.search(args.query)
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = SearchListRecyclerViewAdapter {
//            (activity as MainActivity).loadVideoCover(it)
            (activity as MainActivity)
                .createAndStartPlayList(recyclerAdapter.currentList, it, PlayerType.YTPlayer())
        }
        val recyclerView = binding.searchRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setDivider(R.drawable.line_divider)
    }
}