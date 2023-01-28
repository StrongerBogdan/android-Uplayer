package com.bogdanmurzin.uplayer.util

sealed class Event {
    data class OpenSearchFragment(val query: String) : Event()
    data class Error(val exception: Exception) : Event()
}
