package com.shannon.shannonweek14.data.repository

import com.shannon.shannonweek14.data.service.ApiClient


class SongsQuizRepository(private val token: String? = null) {

    private val api = ApiClient.getClient().create(SongsService::class.java)

    // GET songs, optional Authorization
    suspend fun getSongsQuiz() =
        if (token != null) {
            api.getSongsQuizAuth("Bearer $token")
        } else {
            api.getSongsQuiz()
        }

    // POST answer
    suspend fun postSongAnswer(songId: Int, selectedAnswer: String) =
        api.postSongAnswer(mapOf(
            "songId" to songId,
            "selectedAnswer" to selectedAnswer
        ), "Bearer $token")
}
