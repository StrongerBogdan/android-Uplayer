package com.bogdanmurzin.uplayer.network.utils

import com.bogdanmurzin.uplayer.BuildConfig

object NetworkConstants {

    // YT API
    const val BASE_URL_YOUTUBE = "https://youtube.googleapis.com/youtube/v3/"
    const val HEADER_AUTH_YOUTUBE = "key"
    const val APY_KEY_YT = "Bearer " + BuildConfig.API_KEY
    const val ANDROID_CERT = "X-Android-Cert"
    const val CERT = "23BB78E4CF7108BAC740FCAB457FFE5DA4C1CA04"
    const val ANDROID_PACKAGE_NAME = "X-Android-Package"
    const val PACKAGE_NAME = "com.bogdanmurzin.uplayer"

}