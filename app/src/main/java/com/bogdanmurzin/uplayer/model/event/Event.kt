package com.bogdanmurzin.uplayer.model.event

sealed class Event {
    data class OpenSearchFragment(val query: String) : Event()
    data class Error(val exception: Exception) : Event()
}
