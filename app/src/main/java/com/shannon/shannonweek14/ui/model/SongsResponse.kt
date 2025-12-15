package com.shannon.shannonweek14.data.model

data class SongsResponse(
    val status: String,
    val message: String,
    val data: List<Songs>
)
