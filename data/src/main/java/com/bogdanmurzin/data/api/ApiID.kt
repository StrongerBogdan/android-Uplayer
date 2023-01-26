package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class ApiID(
    @SerializedName("videoId") val videoId: String
)