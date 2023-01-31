package com.bogdanmurzin.uplayer.util

import androidx.viewbinding.ViewBinding
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.databinding.FragmentNowPlayingBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker

class CustomYouTubePlayerListener(
    playerUi: ViewBinding,
    private val youTubePlayer: YouTubePlayer
) : AbstractYouTubePlayerListener() {

    private val playerTracker: YouTubePlayerTracker = YouTubePlayerTracker()
    private val binding = (playerUi as FragmentNowPlayingBinding)

    init {
        youTubePlayer.addListener(playerTracker)
        initViews()
    }

    private fun initViews() {
        // Play pause button listener
        val playPauseButton = binding.playPauseButton
        playPauseButton.setOnClickListener {
            if (playerTracker.state == PlayerState.PLAYING) {
                youTubePlayer.pause()
                binding.playPauseButton.setImageResource(R.drawable.play_icon)
            } else {
                youTubePlayer.play()
                binding.playPauseButton.setImageResource(R.drawable.pause_icon)
            }
        }
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        YouTubePlayerTracker()
        val playerTracker = YouTubePlayerTracker()
        youTubePlayer.addListener(playerTracker)
    }
}