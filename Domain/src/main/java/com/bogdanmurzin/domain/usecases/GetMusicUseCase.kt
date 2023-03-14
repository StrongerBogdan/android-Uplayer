package com.bogdanmurzin.domain.usecases

import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.domain.repositories.MusicRepository
import javax.inject.Inject

class GetMusicUseCase @Inject constructor(private val musicRepository: MusicRepository) {

    suspend operator fun invoke(): List<LocalMusic> = musicRepository.getAllMusic()
}