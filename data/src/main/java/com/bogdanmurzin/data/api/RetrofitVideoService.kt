package com.bogdanmurzin.data.api

import com.bogdanmurzin.data.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitVideoService {

    @GET("videos")
    suspend fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("hl") hl: String = "uk_UA",
        @Query("maxResults") maxResults: Int = 25,
        @Query("regionCode") regionCode: String = "UA",
        @Query("videoCategoryId") videoCategoryId: Int = 10, // 10 - music videos
        @Query("key") key: String = BuildConfig.API_KEY
    ): VideoApiResponseCharts

    @GET("search")
    suspend fun getVideosWithQuery(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 10,
        @Query("q") q: String,
        @Query("type") type: String = "video",
        @Query("videoCategoryId") videoCategoryId: Int = 10, // 10 - music videos
        @Query("key") key: String = BuildConfig.API_KEY
    ): VideoApiResponseSearch
}