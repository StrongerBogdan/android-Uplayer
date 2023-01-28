package com.bogdanmurzin.uplayer.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.NavGraphDirections
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.ActivityMainBinding
import com.bogdanmurzin.uplayer.databinding.NowPlayingBinding
import com.bogdanmurzin.uplayer.service.MusicPlayerService
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import com.bogdanmurzin.uplayer.util.CustomYouTubePlayerListener
import com.bogdanmurzin.uplayer.util.Event
import com.bogdanmurzin.uplayer.util.PlayList
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var youTubePlayer: YouTubePlayer
    private val viewModel: MainViewModel by viewModels()
    private lateinit var listener: YouTubePlayerListener
    private lateinit var navController: NavController

    // Service
    private lateinit var mService: MusicPlayerService
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        youTubePlayerView = binding.nowPlaying.youtubePlayerView
        setContentView(binding.root)
        // Get navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Setup viewModel observe
        setupViewModel()
        // Setup bottom navigation with navgraph
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        initYouTubePlayerView()
    }

    private fun setupViewModel() {
        viewModel.videoList.observe(this) { (videoList, currentVideo) ->
            // mBound = true when YT player is ready, and we can use it in service
            if (mBound) {
                loadVideoCover(currentVideo)
                createAndStartPlayList(videoList, currentVideo)
            }
        }
        viewModel.action.observe(this) { event ->
            if (event is Event.OpenSearchFragment) {
                navController.navigate(NavGraphDirections.actionGlobalSearchResultFragment(event.query))
            }
        }
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MusicPlayerService.LocalBinder
            mService = binder.getService()
            mBound = true
            mService.setPlayer(youTubePlayer)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mBound = false
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    // Loads title, channelName and video image
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

    private fun initYouTubePlayerView() {
        val customPlayerUi: NowPlayingBinding = binding.nowPlaying

        listener = object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@MainActivity.youTubePlayer = youTubePlayer
                // Create and add custom listener
                val customPlayerUiController =
                    CustomYouTubePlayerListener(customPlayerUi, youTubePlayer)
                youTubePlayer.addListener(customPlayerUiController)
                // Create and add seek listener
                val seekBar = binding.nowPlaying.seekbar
                seekBar.youtubePlayerSeekBarListener = object : YouTubePlayerSeekBarListener {
                    override fun seekTo(time: Float) = youTubePlayer.seekTo(time)
                }
                youTubePlayer.addListener(seekBar)
                // Bind to MusicPlayerService after YouTube player is ready
                Intent(this@MainActivity, MusicPlayerService::class.java).also { intent ->
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
            }
        }
        // disable web ui
        val options = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerView.initialize(listener, options)
        youTubePlayerView.enableBackgroundPlayback(true)
    }

    private fun createAndStartPlayList(videoIdsList: List<VideoItem>, currentVideoId: VideoItem) {
        PlayList(videoIdsList, currentVideoId).nextVideoId?.let { video ->
            mService.loadVideo(lifecycle, video)
            startService(Intent(applicationContext, MusicPlayerService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
        Intent(this, MusicPlayerService::class.java).also { stopService(it) }
    }
}