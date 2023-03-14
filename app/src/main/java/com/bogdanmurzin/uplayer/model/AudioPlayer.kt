package com.bogdanmurzin.uplayer.model

import android.content.ContentUris
import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.domain.entities.Music
import com.bogdanmurzin.uplayer.common.PlayerConstants
import com.bogdanmurzin.uplayer.model.playlist.PlayList

class AudioPlayer(private val player: MediaPlayer, private val appContext: Context) : Player {

    private var currentPlaylist: PlayList? = null
    private var listener: CustomPlayerListener? = null

    override fun play() {
        currentPlaylist?.let {
            listener?.onStateChange(it.currentMusic, PlayerConstants.PlayerState.PLAYING)
        }
        player.start()
    }

    override fun pause() {
        currentPlaylist?.let {
            listener?.onStateChange(it.currentMusic, PlayerConstants.PlayerState.PAUSED)
        }
        player.pause()
    }

    override fun next(): Music? {
        val nextMusic = currentPlaylist?.nextMusic()
        if (nextMusic != null && nextMusic is LocalMusic) {
            return loadAudio(nextMusic)
        }
        return null
    }

    override fun prev(): Music? {
        val prevMusic = currentPlaylist?.prevMusic()
        if (prevMusic != null && prevMusic is LocalMusic) {
            return loadAudio(prevMusic)
        }
        return null
    }

    override fun loadPlaylist(playlist: PlayList): Music? {
        currentPlaylist = playlist
        val currentMusic = playlist.currentMusic
        if (currentMusic is LocalMusic) {
            return loadAudio(currentMusic)
        }
        return null
    }

    private fun loadAudio(audio: LocalMusic): LocalMusic {
        listener?.onStateChange(audio, PlayerConstants.PlayerState.PLAYING)
        val audioUri = ContentUris.withAppendedId(CONTENT_URI, audio.id)
        with(player) {
            reset()
            setDataSource(appContext, audioUri)
            prepare()
            start()
        }
        return audio
    }

    override fun addListener(listener: CustomPlayerListener): Boolean {
        this.listener = listener
        return true
    }

    override fun stop() {
        player.pause()
    }

    companion object {
        private val CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }
}