package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class ApiVideo(
    @SerializedName("id") val id: String, // VideoID
    @SerializedName("snippet") val snippet: ApiSnippet,
)
