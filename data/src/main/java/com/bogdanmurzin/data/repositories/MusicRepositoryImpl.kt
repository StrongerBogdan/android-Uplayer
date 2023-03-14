package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.domain.repositories.MusicRepository
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val localDataSource: MusicLocalDataSource
) : MusicRepository {

    override suspend fun getAllMusic(): List<LocalMusic> = localDataSource.getAllMusic()

}