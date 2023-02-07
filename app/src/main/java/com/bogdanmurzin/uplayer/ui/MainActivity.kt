package com.bogdanmurzin.uplayer.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.ActivityMainBinding
import com.bogdanmurzin.uplayer.databinding.FragmentNowPlayingBinding
import com.bogdanmurzin.uplayer.service.YoutubePlayerService
import com.bogdanmurzin.uplayer.util.CustomYouTubePlayerListener
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var youTubePlayer: YouTubePlayer

    // Service
    private lateinit var mService: YoutubePlayerService
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        youTubePlayerView = binding.nowPlaying.youtubePlayerView
        setContentView(binding.root)

        // Get navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup bottom navigation with navgraph
        binding.bottomNavigation.setupWithNavController(navController)

        initYouTubePlayerView()
    }

    // Loads title, channelName and video image
    //TODO rework
    fun loadVideoCover(currentVideo: VideoItem) {
        binding.playNowFrame.visibility = View.VISIBLE
        with(binding.nowPlaying) {
            videoName.text = currentVideo.title
            authorName.text = currentVideo.author
            Glide.with(binding.root.context)
                .load(currentVideo.imageUrl)
                .override(Constants.CHARTS_IMG_WIDTH, Constants.CHARTS_IMG_HEIGHT)
                .centerCrop()
                .into(videoPreview)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
        Intent(this, YoutubePlayerService::class.java).also { stopService(it) }
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as YoutubePlayerService.LocalBinder
            mService = binder.getService()
            mBound = true
            mService.setPlayer(youTubePlayer)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mBound = false
        }
    }

    private fun initYouTubePlayerView() {
        val customPlayerUi: FragmentNowPlayingBinding = binding.nowPlaying

        val listener = object : AbstractYouTubePlayerListener() {

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
                Intent(this@MainActivity, YoutubePlayerService::class.java).also { intent ->
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)

                val keyDown: KeyEvent? = when (state) {
                    PlayerConstants.PlayerState.PAUSED ->
                        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
                    PlayerConstants.PlayerState.PLAYING ->
                        KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
                    else -> null
                }

                Intent(this@MainActivity, YoutubePlayerService::class.java).also { intent ->
                    keyDown?.let {
                        intent.action = Intent.ACTION_MEDIA_BUTTON
                        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyDown)
                    }
                    startService(intent)
                }
            }
        }
        // disable web ui
        val options = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerView.initialize(listener, options)
        youTubePlayerView.enableBackgroundPlayback(true)
    }

    fun createAndStartPlayList(videoIdsList: List<VideoItem>, currentVideoId: VideoItem) {
//        PlayList(videoIdsList, currentVideoId).nextVideoId?.let { video ->
//            mService.loadVideo(lifecycle, video)
//            loadVideoCover(video)
//            startService(Intent(applicationContext, YoutubePlayerService::class.java))
//        }
        // mBound = true when YT player is ready, and we can use it in service
        if (mBound) {
            mService.loadVideo(lifecycle, currentVideoId)
            loadVideoCover(currentVideoId)
            startService(Intent(applicationContext, YoutubePlayerService::class.java))
        }
    }
}