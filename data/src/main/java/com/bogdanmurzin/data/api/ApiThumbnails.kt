package com.bogdanmurzin.data.api

import com.google.gson.annotations.SerializedName

data class ApiThumbnails(
    @SerializedName("medium") val medium: ApiMedium
)
