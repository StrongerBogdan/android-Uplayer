package com.bogdanmurzin.domain.usecases

import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.repositories.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosWithQueryUseCase @Inject constructor(private val videoRepository: VideoRepository) {

    suspend operator fun invoke(query: String, maxResults: Int): Flow<List<VideoItem>> =
        videoRepository.getListOfVideosWithQuery(query, maxResults)
}