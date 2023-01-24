package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.repositories.VideoRemoteDataSource
import com.bogdanmurzin.data.repositories.VideoRemoteDataSourceImpl
import com.bogdanmurzin.data.repositories.VideoRepositoryImpl
import com.bogdanmurzin.domain.repositories.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoBindModule {

    @Binds
    abstract fun bindVideoRepository(repo: VideoRepositoryImpl): VideoRepository

    @Binds
    abstract fun bindVideoRemoteDataSource(dataSource: VideoRemoteDataSourceImpl): VideoRemoteDataSource
}