package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.LocalMusic

interface MusicRepository {
    suspend fun getAllMusic(): List<LocalMusic>
}