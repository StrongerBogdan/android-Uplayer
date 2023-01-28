package com.bogdanmurzin.uplayer.util

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

}