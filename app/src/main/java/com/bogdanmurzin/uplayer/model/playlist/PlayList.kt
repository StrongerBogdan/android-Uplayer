package com.bogdanmurzin.uplayer.model.playlist

import com.bogdanmurzin.domain.entities.Music

class PlayList(private val musicList: List<Music>, pickedVideo: Music) {

    private var position = musicList.indexOf(pickedVideo)

    val currentMusic: Music
        get() = musicList[position]

    fun nextMusic(): Music? =
        if (hasNext()) musicList[++position] else null

    fun prevMusic(): Music? =
        if (hasPrev()) musicList[--position] else null

    private fun hasNext() = position < musicList.size - 1

    private fun hasPrev() = position > 0
}