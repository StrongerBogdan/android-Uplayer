package com.bogdanmurzin.uplayer.model.player

import com.bogdanmurzin.domain.entities.Music
import com.bogdanmurzin.uplayer.common.PlayerConstants


interface CustomPlayerListener {
    fun onStateChange(music: Music, state: PlayerConstants.PlayerState)
}