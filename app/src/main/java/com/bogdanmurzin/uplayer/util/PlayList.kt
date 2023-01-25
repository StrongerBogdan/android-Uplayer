package com.bogdanmurzin.uplayer.util

import com.bogdanmurzin.uplayer.common.Constants.EMPTY_STRING

class PlayList(videoIds: List<String>, currentVideoId: String) {

    private val position: Int = videoIds.indexOf(currentVideoId)

    private val iterator = videoIds.listIterator(position)

    val nextVideoId = if (iterator.hasNext()) iterator.next() else EMPTY_STRING

    val previousVideoId = if (iterator.hasPrevious()) iterator.previous() else EMPTY_STRING
}