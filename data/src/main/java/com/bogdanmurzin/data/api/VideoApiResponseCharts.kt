package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class VideoApiResponseCharts(
    @SerializedName("items") val items: ArrayList<ApiVideo>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
