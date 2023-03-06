package com.bogdanmurzin.uplayer.ui.local_music

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants.PERMISSION_REQUEST_CODE
import com.bogdanmurzin.uplayer.common.Constants.TAG
import com.bogdanmurzin.uplayer.databinding.FragmentLocalMusicBinding
import com.bogdanmurzin.uplayer.ui.local_music.adapter.LocalMusicRecyclerViewAdapter
import com.bogdanmurzin.uplayer.util.extension.RecyclerViewExtension.setDivider
import com.bogdanmurzin.uplayer.viewmodel.local_music.LocalMusicViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocalMusicFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentLocalMusicBinding
    private lateinit var recyclerAdapter: LocalMusicRecyclerViewAdapter
    private val viewModel by viewModels<LocalMusicViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    private fun checkPermissions() {
        if (EasyPermissions.hasPermissions(requireContext(), READ_EXTERNAL_STORAGE)) {
            setupLocalMusic()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                host = this,
                rationale = getString(R.string.pmx_default_description),
                requestCode = PERMISSION_REQUEST_CODE,
                READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun setupLocalMusic() {
        setupRecycler()

        viewModel.musicList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                recyclerAdapter.submitList(it)
            }
        }

        // query audio files here
        viewModel.fetchLocalMusic()
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerAdapter = LocalMusicRecyclerViewAdapter {
            //TODO on recycler item clicked
//            (activity as MainActivity).createAndStartPlayList(recyclerAdapter.currentList, it)
        }

        with(binding.localMusicRecycler) {
            adapter = recyclerAdapter
            this.layoutManager = layoutManager
            setDivider(R.drawable.line_divider)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied: $requestCode :${perms.size}")

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            with(SettingsDialog.Builder(requireActivity())) {
                rationale(R.string.pmx_to_settings_description)
                title(R.string.pmx_request_dialog_title)
                positiveButtonText(R.string.pmx_btn_request_accept)
                negativeButtonText(R.string.cancel)
                build()
            }.show()

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                host = this,
                rationale = getString(R.string.pmx_default_description),
                requestCode = PERMISSION_REQUEST_CODE,
                READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsGranted: $requestCode :${perms.size}")
        setupLocalMusic()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult: ${grantResults[0]}")
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}