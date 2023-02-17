package com.bogdanmurzin.uplayer.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultCoroutineDispatcherProvider @Inject constructor() : CoroutineDispatcherProvider {

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun computation(): CoroutineDispatcher = Dispatchers.Default
}