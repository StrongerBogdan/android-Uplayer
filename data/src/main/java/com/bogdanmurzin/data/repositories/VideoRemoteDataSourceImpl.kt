package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.data.api.RetrofitVideoService
import com.bogdanmurzin.data.mapper.VideoApiResponseMapper
import com.bogdanmurzin.domain.entities.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoRemoteDataSourceImpl @Inject constructor(
    private val retrofitService: RetrofitVideoService,
    private val mapper: VideoApiResponseMapper
) : VideoRemoteDataSource {

    override suspend fun getRemoteVideos(maxResults: Int): Flow<List<VideoItem>> =
        withContext(Dispatchers.IO) {
            val response = retrofitService.getVideos(maxResults = maxResults)
            return@withContext flow {
                emit(
                    response.items.map { videoApiItem -> mapper.toVideoList(videoApiItem) }
                )
            }
        }
}