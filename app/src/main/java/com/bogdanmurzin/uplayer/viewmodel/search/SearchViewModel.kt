package com.bogdanmurzin.uplayer.viewmodel.search

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.usecases.GetVideosWithQueryUseCase
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.di.DefaultCoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getVideosWithQueryUseCase: GetVideosWithQueryUseCase,
    private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
) : ViewModel() {

    private val _videoList: MutableLiveData<List<VideoItem>> = MutableLiveData()
    var videoList: LiveData<List<VideoItem>> = _videoList

    fun search(query: String) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            loadVideos(query)
        }
    }

    // Load videos by query
    private suspend fun loadVideos(query: String) {
        getVideosWithQueryUseCase.invoke(query, Constants.SEARCH_VIDEO_COUNT).collect {
            _videoList.postValue(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getVideosWithQueryUseCase: GetVideosWithQueryUseCase,
        private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(
                getVideosWithQueryUseCase,
                coroutineDispatcherProvider
            ) as T
    }
}