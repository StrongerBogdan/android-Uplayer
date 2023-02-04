package com.bogdanmurzin.uplayer.service.callback

import android.support.v4.media.session.MediaSessionCompat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer


class MediaSessionCallback(private val player: YouTubePlayer) : MediaSessionCompat.Callback() {

    override fun onPlay() {
        player.play()
    }

    override fun onPause() {
        player.pause()
    }

    override fun onSeekTo(position: Long) {
        player.seekTo(position.toFloat())
    }
}