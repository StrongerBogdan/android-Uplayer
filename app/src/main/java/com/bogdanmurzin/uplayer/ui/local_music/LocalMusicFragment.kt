package com.bogdanmurzin.uplayer.ui.local_music

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.databinding.FragmentLocalMusicBinding
import com.bogdanmurzin.uplayer.ui.local_music.adapter.LocalMusicRecyclerViewAdapter
import com.bogdanmurzin.uplayer.util.extension.RecyclerViewExtension.setDivider
import com.bogdanmurzin.uplayer.viewmodel.local_music.LocalMusicViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocalMusicFragment : Fragment() {

    private lateinit var binding: FragmentLocalMusicBinding
    private lateinit var recyclerAdapter: LocalMusicRecyclerViewAdapter
    private val viewModel by viewModels<LocalMusicViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // TODO require permission
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        } else {
            // Permission is already granted

            setupRecycler()

            viewModel.musicList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    recyclerAdapter.submitList(it)
                }
            }

            // query audio files here
            viewModel.fetchLocalMusic()
        }
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = LocalMusicRecyclerViewAdapter {
            //TODO on recycler item clicked
//            (activity as MainActivity).createAndStartPlayList(recyclerAdapter.currentList, it)
        }
        val recyclerView = binding.localMusicRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setDivider(R.drawable.line_divider)
    }
}