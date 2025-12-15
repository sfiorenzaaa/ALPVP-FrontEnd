package com.shannon.shannonweek14.ui.model

data class EmojiQuiz(
    val id: Int,
    val emoji: String,
    val options: List<String>,
    val correctAnswer: String
)
