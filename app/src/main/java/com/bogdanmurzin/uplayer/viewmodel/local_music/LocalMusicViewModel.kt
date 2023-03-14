package com.bogdanmurzin.uplayer.viewmodel.local_music

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdanmurzin.domain.entities.LocalMusic
import com.bogdanmurzin.domain.usecases.GetMusicUseCase
import com.bogdanmurzin.uplayer.di.DefaultCoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(
    private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider,
    private val getMusicUseCase: GetMusicUseCase
) : ViewModel() {

    private val _musicList = MutableLiveData<List<LocalMusic>>()
    var musicList: LiveData<List<LocalMusic>> = _musicList

    fun fetchLocalMusic() = viewModelScope.launch(coroutineDispatcherProvider.io()) {
        _musicList.postValue(getMusicUseCase.invoke().also { Log.d("TAGGY", "fetchLocalMusic: $it") })
    }
}