package com.bogdanmurzin.uplayer.util

import androidx.viewbinding.ViewBinding
import com.bogdanmurzin.uplayer.databinding.NowPlayingBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker

class CustomYouTubePlayerListener(
    playerUi: ViewBinding,
    private val youTubePlayer: YouTubePlayer
) :
    AbstractYouTubePlayerListener() {
    private val playerTracker: YouTubePlayerTracker = YouTubePlayerTracker()
    private val binding = (playerUi as NowPlayingBinding)

    init {
        youTubePlayer.addListener(playerTracker)
        initViews()
    }

    private fun initViews() {
        val playPauseButton = binding.playPauseButton
        playPauseButton.setOnClickListener {
            if (playerTracker.state == PlayerState.PLAYING) youTubePlayer.pause() else youTubePlayer.play()
        }
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        YouTubePlayerTracker()
        val playerTracker = YouTubePlayerTracker()
        youTubePlayer.addListener(playerTracker)
        // Play pause button listener
        binding.playPauseButton.setOnClickListener {
            if (playerTracker.state == PlayerState.PLAYING) youTubePlayer.pause() else youTubePlayer.play()
        }
    }
}