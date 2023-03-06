package com.bogdanmurzin.uplayer.model.playlist

import com.bogdanmurzin.domain.entities.Music

class PlayList(private val musicList: List<Music>, pickedVideo: Music) {

    private var position = musicList.indexOf(pickedVideo)

    val currentMusic: Music
        get() = musicList[position - 1]

    fun nextMusic(): Music? {
        if (position + 1 > musicList.size) return null
        return musicList[position++]
    }

    fun prevMusic(): Music? {
        if (position - 1 < 0) return null
        return musicList[--position]
    }
}