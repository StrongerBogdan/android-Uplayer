package com.bogdanmurzin.uplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bogdanmurzin.uplayer.databinding.FragmentHeaderBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentHeaderBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeaderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchBarInputLayout.setEndIconOnClickListener {
            val searchText = binding.searchBarEditText.text
            if (!searchText.isNullOrEmpty()) mainViewModel.openSearchFragment(searchText.toString())
        }
    }
}