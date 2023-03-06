package com.bogdanmurzin.domain.entities

data class LocalMusic(
    val id: Long,
    override val title: String,
    override val author: String,
    override val coverArtUri: String
) : Music()

