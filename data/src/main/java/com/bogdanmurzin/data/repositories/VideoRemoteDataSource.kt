package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.domain.entities.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRemoteDataSource {

    suspend fun getRemoteVideos(maxResults: Int): Flow<List<VideoItem>>
}