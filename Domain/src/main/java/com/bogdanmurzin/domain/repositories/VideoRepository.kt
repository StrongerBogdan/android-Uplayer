package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    suspend fun getListOfVideos(maxResults: Int): Flow<List<VideoItem>>

    suspend fun getListOfVideosWithQuery(query: String, maxResults: Int): Flow<List<VideoItem>>
}