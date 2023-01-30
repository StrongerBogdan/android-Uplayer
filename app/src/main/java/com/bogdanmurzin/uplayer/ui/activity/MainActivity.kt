package com.bogdanmurzin.uplayer.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.ActivityMainBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import com.bogdanmurzin.uplayer.util.Event
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Setup bottom navigation with navgraph
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupViewModel() {
        viewModel.videoList.observe(this) { (videoList, currentVideo) ->
            // When we gets list of Videos and current video
            // TODO implement
        }
        viewModel.action.observe(this) { event ->
            // TODO navigate to Search
//            if (event is Event.OpenSearchFragment) {
//                navController.navigate(NavGraphDirections.actionGlobalSearchResultFragment(event.query))
//            }
        }
    }

    // Loads title, channelName and video image
    //TODO rework
    private fun loadVideoCover(currentVideo: VideoItem) {
        binding.playNowFrame.visibility = View.VISIBLE
        with(binding.nowPlaying.nowPlayingCover) {
            videoName.text = currentVideo.title
            authorName.text = currentVideo.author
            Glide.with(binding.root.context)
                .load(currentVideo.imageUrl)
                .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
                .centerCrop()
                .into(videoPreview)
        }
    }
}