package com.bogdanmurzin.uplayer.di

import com.bogdanmurzin.uplayer.util.CoroutineDispatcherProvider
import com.bogdanmurzin.uplayer.util.DefaultCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindModule {

    @Binds
    abstract fun bindCoroutineDispatcherProvider(impl: DefaultCoroutineDispatcherProvider): CoroutineDispatcherProvider
}