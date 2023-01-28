package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class ApiSnippet(
    @SerializedName("title") val title: String,
    @SerializedName("channelTitle") val channelTitle: String,
    @SerializedName("thumbnails") val thumbnails: ApiThumbnails,
)
