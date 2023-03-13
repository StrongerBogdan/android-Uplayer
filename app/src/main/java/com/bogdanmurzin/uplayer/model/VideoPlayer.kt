package com.bogdanmurzin.uplayer.model

import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.uplayer.common.PlayerConstants
import com.bogdanmurzin.uplayer.model.playlist.PlayList
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

class VideoPlayer(private val player: YouTubePlayer) : Player {

    private var currentPlaylist: PlayList? = null
    private var listener: CustomPlayerListener? = null


    /**
     * Loads the next video item from the given playlist and returns it.
     *
     * @param playlist the [PlayList] to load from
     * @return the started item to play, or null if there are no more video items in the playlist
     */
    override fun loadPlaylist(playlist: PlayList): VideoItem? {
        currentPlaylist = playlist
        val nextMusic = playlist.currentMusic
        if (nextMusic is VideoItem) {
            return loadVideo(nextMusic)
        }
        return null
    }

    private fun loadVideo(video: VideoItem): VideoItem {
        listener?.onStateChange(video, PlayerConstants.PlayerState.PLAYING)
        player.loadVideo(video.videoId, 0f)
        return video
    }

    override fun play() {
        currentPlaylist?.let {
            listener?.onStateChange(it.currentMusic, PlayerConstants.PlayerState.PLAYING)
        }
        player.play()
    }

    override fun pause() {
        currentPlaylist?.let {
            listener?.onStateChange(it.currentMusic, PlayerConstants.PlayerState.PAUSED)
        }
        player.pause()
    }

    override fun next(): VideoItem? {
        val nextMusic = currentPlaylist?.nextMusic()
        if (nextMusic != null && nextMusic is VideoItem) {
            return loadVideo(nextMusic)
        }
        return null
    }

    override fun prev(): VideoItem? {
        val prevMusic = currentPlaylist?.prevMusic()
        if (prevMusic != null && prevMusic is VideoItem) {
            return loadVideo(prevMusic)
        }
        return null
    }

    override fun addListener(listener: CustomPlayerListener): Boolean {
        this.listener = listener
        return true
    }
}