package com.bogdanmurzin.uplayer.model.player

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

sealed class PlayerType {
    class YTPlayer : PlayerType() {
        lateinit var youtubePlayer: YouTubePlayer
    }

    object LocalPlayer : PlayerType()
}

