package com.shannon.shannonweek14.data.model

data class Songs(
    val id: Int,
    val clue: String,
    val options: List<String>,
    val correctAnswer: String
)
