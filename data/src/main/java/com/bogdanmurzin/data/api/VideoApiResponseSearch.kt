package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

// Using different data class because youTubeApi returns a little different pattern for videoIds
data class VideoApiResponseSearch(
    @SerializedName("items") val items: ArrayList<ApiVideoSearch>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
