package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.repositories.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val remoteDataSource: VideoRemoteDataSource
) : VideoRepository {

    override suspend fun getListOfVideos(maxResults: Int): Flow<List<VideoItem>> =
        remoteDataSource.getRemoteVideos(maxResults)

}