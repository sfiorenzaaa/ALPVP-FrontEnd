package com.shannon.shannonweek14.data.model

import com.shannon.shannonweek14.ui.model.Songs

data class SongsResponse(
    val status: String,
    val message: String,
    val data: List<Songs>
)
