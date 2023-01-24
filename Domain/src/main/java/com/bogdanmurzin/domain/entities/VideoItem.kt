package com.bogdanmurzin.domain.entities

data class VideoItem(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String,
    val videoId: String
)