package com.bogdanmurzin.uplayer.model.playlist

import com.bogdanmurzin.domain.entities.VideoItem

class PlayList(videoList: List<VideoItem>, currentVideo: VideoItem) {

    private val position: Int = videoList.indexOf(currentVideo)

    private val iterator = videoList.listIterator(position)

    val nextVideoId: VideoItem? = if (iterator.hasNext()) iterator.next() else null

    val previousVideoId: VideoItem? = if (iterator.hasPrevious()) iterator.previous() else null
}