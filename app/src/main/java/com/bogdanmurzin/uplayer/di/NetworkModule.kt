package com.bogdanmurzin.uplayer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideLoggerInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor().level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideGsonConvertorFactory(): GsonConverterFactory =
        GsonConverterFactory.create()
}