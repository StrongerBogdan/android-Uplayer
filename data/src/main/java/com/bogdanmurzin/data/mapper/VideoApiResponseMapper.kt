package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.api.ApiVideo
import com.bogdanmurzin.domain.entities.VideoItem
import javax.inject.Inject

class VideoApiResponseMapper @Inject constructor() {

    fun toVideoList(apiVideo: ApiVideo): VideoItem {
        return with(apiVideo) {
            val videoId = id
            val title = snippet.title
            val authorName = snippet.channelTitle
            val imageUrl = snippet.thumbnails.medium.url
            VideoItem(videoId, title, authorName, imageUrl)
        }
    }
}