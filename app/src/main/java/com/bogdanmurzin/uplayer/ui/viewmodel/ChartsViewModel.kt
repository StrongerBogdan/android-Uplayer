package com.bogdanmurzin.uplayer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdanmurzin.domain.entities.VideoItem
import com.bogdanmurzin.domain.usecases.GetVideosUseCase
import com.bogdanmurzin.uplayer.common.Constants
import com.bogdanmurzin.uplayer.util.DefaultCoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
) : ViewModel() {

    private val _chartList = MutableLiveData<List<VideoItem>>()
    var chartList: LiveData<List<VideoItem>> = _chartList
    private val _secondChartList: MutableLiveData<List<VideoItem>> = MutableLiveData()
    var secondChartList: LiveData<List<VideoItem>> = _secondChartList

    fun fetchCharts() = viewModelScope.launch(coroutineDispatcherProvider.io()) {
        getChartList(Constants.CHARTS_VIDEO_COUNT).collect { _chartList.postValue(it) }
        getChartList(Constants.SECOND_CHARTS_VIDEO_COUNT).collect { _secondChartList.postValue(it) }
    }

    private suspend fun getChartList(maxResults: Int): Flow<List<VideoItem>> =
        getVideosUseCase.invoke(maxResults)

}