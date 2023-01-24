package com.bogdanmurzin.uplayer.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.usecases.GetVideosUseCase
import com.bogdanmurzin.uplayer.common.Constants.CHARTS_VIDEO_COUNT
import com.bogdanmurzin.uplayer.common.Constants.SECOND_CHARTS_VIDEO_COUNT
import com.bogdanmurzin.uplayer.util.DefaultCoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    fun updateVideoList() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            getChartList(CHARTS_VIDEO_COUNT).collect { _chartList.postValue(it) }
            getChartList(SECOND_CHARTS_VIDEO_COUNT).collect { _secondChartList.postValue(it) }
        }
    }

    private suspend fun getChartList(maxResults: Int): Flow<List<VideoItem>> =
        getVideosUseCase.invoke(maxResults)


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