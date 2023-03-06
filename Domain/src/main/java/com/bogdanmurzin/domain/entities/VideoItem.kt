package com.bogdanmurzin.domain.entities

data class VideoItem(
    val videoId: String,
    override val title: String,
    override val author: String,
    override val coverArtUri: String
) : Music()