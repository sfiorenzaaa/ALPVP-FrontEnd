package com.shannon.shannonweek14.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shannon.shannonweek14.data.model.EmojiQuiz

class EmojiQuizViewModel : ViewModel() {

    // List soal lokal
    val quizList = listOf(
        EmojiQuiz(1,"😴💤", listOf("Sleep","Dream","Nap","Tired"),"Sleep"),
        EmojiQuiz(2,"🔥📚", listOf("Study Hard","Fire Book","Hot Topic","Smart"),"Study Hard"),
        EmojiQuiz(3,"🍔➡️🍟", listOf("Fast Food","Meal","Snack","Burger Fries"),"Burger Fries"),
        EmojiQuiz(4,"🎬🍿", listOf("Movie Night","Cinema","Popcorn Time","Film"),"Movie Night"),
        EmojiQuiz(5,"💔😭", listOf("Heartbroken","Sad Love","Breakup","Pain"),"Heartbroken")
        // Tambahkan sisa 15 soal sesuai JSON
    )

    var currentIndex = mutableStateOf(0)
    var score = mutableStateOf(0)
    var selectedAnswer = mutableStateOf<String?>(null)
    var showResult = mutableStateOf(false)

    fun selectAnswer(answer: String) {
        selectedAnswer.value = answer
        if (answer == quizList[currentIndex.value].correctAnswer) {
            score.value += 1
        }
    }

    fun nextQuestion() {
        selectedAnswer.value = null
        if (currentIndex.value < quizList.size - 1) {
            currentIndex.value += 1
        } else {
            showResult.value = true
        }
    }

    fun resetQuiz() {
        currentIndex.value = 0
        score.value = 0
        selectedAnswer.value = null
        showResult.value = false
    }
}
