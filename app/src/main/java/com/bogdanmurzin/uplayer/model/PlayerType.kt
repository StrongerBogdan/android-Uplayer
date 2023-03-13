package com.bogdanmurzin.uplayer.model

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

sealed class PlayerType {
    class YTPlayer : PlayerType() {
        lateinit var youtubePlayer: YouTubePlayer
    }

    object LocalPlayer : PlayerType()
}

