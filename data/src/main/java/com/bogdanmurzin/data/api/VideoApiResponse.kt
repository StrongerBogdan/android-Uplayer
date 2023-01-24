package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class VideoApiResponse(
    @SerializedName("items") val items: ArrayList<ApiVideo>,
    @SerializedName("nextPageToken") val nextPageToken: String
)
