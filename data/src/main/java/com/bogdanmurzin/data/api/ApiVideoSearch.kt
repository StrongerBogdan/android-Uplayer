package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class ApiVideoSearch(
    @SerializedName("id") val id: ApiID, // ID
    @SerializedName("snippet") val snippet: ApiSnippet,
)
