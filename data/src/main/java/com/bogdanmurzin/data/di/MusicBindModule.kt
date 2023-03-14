package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.repositories.*
import com.bogdanmurzin.domain.repositories.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicBindModule {

    @Binds
    abstract fun bindMusicRepository(repo: MusicRepositoryImpl): MusicRepository

    @Binds
    abstract fun bindMusicLocalDataSource(dataSource: MusicLocalDataSourceImpl): MusicLocalDataSource
}