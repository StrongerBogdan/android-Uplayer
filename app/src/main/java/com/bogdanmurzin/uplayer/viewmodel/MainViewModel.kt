package com.bogdanmurzin.uplayer.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.usecases.GetVideosUseCase
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_VIDEO_COUNT
import com.bogdanmurzin.uplayer.common.Constants.SECOND_CHARTS_VIDEO_COUNT
import com.bogdanmurzin.uplayer.common.SingleLiveEvent
import com.bogdanmurzin.uplayer.di.DefaultCoroutineDispatcherProvider
import com.bogdanmurzin.uplayer.model.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
) : ViewModel() {

    private val _chartList: MutableLiveData<List<VideoItem>> = MutableLiveData()
    var chartList: LiveData<List<VideoItem>> = _chartList
    private val _secondChartList: MutableLiveData<List<VideoItem>> = MutableLiveData()
    var secondChartList: LiveData<List<VideoItem>> = _secondChartList

    // Provide whole list of videoIds and current (clicked) videoId
    private val _videoList: MutableLiveData<Pair<List<VideoItem>, VideoItem>> = MutableLiveData()
    var videoList: LiveData<Pair<List<VideoItem>, VideoItem>> = _videoList

    private val _action: SingleLiveEvent<Event> = SingleLiveEvent()
    val action: LiveData<Event> = _action

    fun updateVideoList() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            getChartList(CHARTS_VIDEO_COUNT).collect { _chartList.postValue(it) }
            getChartList(SECOND_CHARTS_VIDEO_COUNT).collect { _secondChartList.postValue(it) }
        }
    }

    private suspend fun getChartList(maxResults: Int): Flow<List<VideoItem>> =
        getVideosUseCase.invoke(maxResults)

    fun getVideoIds(videoList: List<VideoItem>, clickedVideoItem: VideoItem) {
        _videoList.postValue(videoList to clickedVideoItem)
    }

    fun openSearchFragment(query: String) {
        _action.postValue(Event.OpenSearchFragment(query))
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getVideosUseCase: GetVideosUseCase,
        private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(
                getVideosUseCase,
                coroutineDispatcherProvider
            ) as T
    }
}