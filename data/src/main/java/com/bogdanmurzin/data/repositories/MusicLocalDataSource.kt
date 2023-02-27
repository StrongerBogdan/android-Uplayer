package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.domain.entities.LocalMusic

interface MusicLocalDataSource {

    suspend fun getAllMusic(): List<LocalMusic>
}