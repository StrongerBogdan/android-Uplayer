package com.bogdanmurzin.uplayer.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.databinding.ActivityMainBinding
import com.bogdanmurzin.uplayer.databinding.NowPlayingBinding
import com.bogdanmurzin.uplayer.ui.viewmodel.MainViewModel
import com.bogdanmurzin.uplayer.util.CustomYouTubePlayerListener
import com.bogdanmurzin.uplayer.util.PlayList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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

        viewModel.videoList.observe(this) {
            loadVideo(it.first, it.second)
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
            }
        }
        // disable web ui
        val options = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerView.initialize(listener, options)
    }

    private fun loadVideo(videoIdsList: List<VideoItem>, currentVideoId: VideoItem) {
        PlayList(videoIdsList, currentVideoId).nextVideoId?.videoId?.let { videoId ->
            youTubePlayer.loadOrCueVideo(lifecycle, videoId, 0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }
}