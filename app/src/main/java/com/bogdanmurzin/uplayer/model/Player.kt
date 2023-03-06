package com.bogdanmurzin.uplayer.model

import com.bogdanmurzin.domain.entities.Music
import com.bogdanmurzin.uplayer.model.playlist.PlayList

interface Player {

    fun play()
    fun pause()
    fun next(): Music?
    fun prev(): Music?
    fun loadPlaylist(playlist: PlayList): Music?
}