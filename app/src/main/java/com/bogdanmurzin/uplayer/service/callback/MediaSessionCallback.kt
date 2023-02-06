package com.bogdanmurzin.uplayer.service.callback

import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.bogdanmurzin.uplayer.common.Constants.TAG
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer


class MediaSessionCallback(
    private val player: YouTubePlayer
) : MediaSessionCompat.Callback() {

    override fun onPlay() {
        Log.d(TAG, "onPlay: Callback")
        player.play()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: Callback")
        player.pause()
    }

    override fun onSeekTo(position: Long) {
        Log.d(TAG, "onSeekTo: Callback")
        player.seekTo(position.toFloat())
    }

    override fun onSkipToNext() {
        Log.d(TAG, "onSkipToNext: Callback")
        super.onSkipToNext()
    }

    override fun onSkipToPrevious() {
        Log.d(TAG, "onSkipToPrevious: Callback")
        super.onSkipToPrevious()
    }
}