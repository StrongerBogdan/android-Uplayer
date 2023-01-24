package com.bogdanmurzin.uplayer.di

import com.bogdanmurzin.data.api.RetrofitVideoService
import com.bogdanmurzin.uplayer.common.Constants.RETROFIT_VIDEO_NAMED
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants.ANDROID_CERT
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants.ANDROID_PACKAGE_NAME
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants.APY_KEY_YT
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants.PACKAGE_NAME
import com.bogdanmurzin.uplayer.network.utils.NetworkConstants.CERT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ApiVideoModule {

    @Provides
    @Named(RETROFIT_VIDEO_NAMED)
    fun provideRetrofitVideo(
        @Named(RETROFIT_VIDEO_NAMED) client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL_YOUTUBE)
        .client(client)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    fun provideRetrofitVideoService(@Named(RETROFIT_VIDEO_NAMED) retrofit: Retrofit)
            : RetrofitVideoService = retrofit.create(RetrofitVideoService::class.java)

    @Provides
    @Named(RETROFIT_VIDEO_NAMED)
    fun provideOkHttpClient(
        @Named(RETROFIT_VIDEO_NAMED) authInterceptor: Interceptor,
        logging: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Named(RETROFIT_VIDEO_NAMED)
    fun provideAuthInterceptor(): Interceptor =
        object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader(NetworkConstants.HEADER_AUTH_YOUTUBE, APY_KEY_YT)
                    .addHeader(ANDROID_CERT, CERT)
                    .addHeader(ANDROID_PACKAGE_NAME, PACKAGE_NAME).build()
                return chain.proceed(request)
            }
        }
}