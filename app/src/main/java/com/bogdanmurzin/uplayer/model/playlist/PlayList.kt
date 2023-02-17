package com.bogdanmurzin.uplayer.model.playlist

import com.bogdanmurzin.domain.entities.VideoItem

class PlayList(private val videoList: List<VideoItem>, pickedVideo: VideoItem) {

    private var position = videoList.indexOf(pickedVideo)

    val currentVideo: VideoItem
        get() = videoList[position - 1]

    fun nextVideo(): VideoItem? {
        if (position + 1 >= videoList.size) return null
        return videoList[position++]
    }

    fun prevVideo(): VideoItem? {
        if (position - 1 < 0) return null
        return videoList[--position]
    }
}