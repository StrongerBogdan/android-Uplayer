package com.bogdanmurzin.uplayer.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.NavGraphDirections
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.databinding.ActivityMainBinding
import com.bogdanmurzin.uplayer.databinding.NowPlayingBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import com.bogdanmurzin.uplayer.util.CustomYouTubePlayerListener
import com.bogdanmurzin.uplayer.util.Event
import com.bogdanmurzin.uplayer.util.PlayList
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        youTubePlayerView = binding.nowPlaying.youtubePlayerView
        setContentView(binding.root)
        initYouTubePlayerView()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        viewModel.videoList.observe(this) { (videoList, currentVideo) ->
            loadVideoCover(currentVideo)
            createAndStartPlayList(videoList, currentVideo)
        }
        viewModel.action.observe(this) {event ->
            if(event is Event.OpenSearchFragment){
                navController.navigate(NavGraphDirections.actionGlobalSearchResultFragment(event.query))
            }
        }
    }

    // Loads title, channelName and video image
    private fun loadVideoCover(currentVideo: VideoItem) {
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
            }
        }
        // disable web ui
        val options = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerView.initialize(listener, options)
        youTubePlayerView.enableBackgroundPlayback(true)
    }

    private fun createAndStartPlayList(videoIdsList: List<VideoItem>, currentVideoId: VideoItem) {
        PlayList(videoIdsList, currentVideoId).nextVideoId?.videoId?.let { videoId ->
            loadVideo(videoId)
        }
    }

    private fun loadVideo(videoId: String) {
        youTubePlayer.loadOrCueVideo(lifecycle, videoId, 0f)
        binding.nowPlaying.playPauseButton.setImageResource(R.drawable.pause_icon)
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }
}