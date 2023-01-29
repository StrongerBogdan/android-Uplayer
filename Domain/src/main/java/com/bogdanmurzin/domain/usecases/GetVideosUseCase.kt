package com.bogdanmurzin.domain.usecases

import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.repositories.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val videoRepository: VideoRepository) {

    suspend operator fun invoke(maxResults: Int): Flow<List<VideoItem>> =
        videoRepository.getListOfVideos(maxResults)
}