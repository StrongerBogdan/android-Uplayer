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
import com.bogdanmurzin.uplayer.databinding.FragmentLocalMusicBinding
import com.bogdanmurzin.uplayer.viewmodel.local_music.LocalMusicViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocalMusicFragment : Fragment() {

    private lateinit var binding: FragmentLocalMusicBinding
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

            viewModel.musicList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
//                    binding.ivText.visibility = View.VISIBLE
//                    Glide.with(binding.root.context)
//                        .load(it[1].albumArtUri)
//                        .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
//                        .centerCrop()
//                        .error(R.drawable.no_item_icon)
//                        .into(binding.ivText)
                }
            }

            // query audio files here
            viewModel.fetchLocalMusic()
        }
    }
}