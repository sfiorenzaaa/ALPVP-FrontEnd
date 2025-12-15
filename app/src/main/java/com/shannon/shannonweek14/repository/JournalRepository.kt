package com.shannon.shannonweek14.data.repository

import com.shannon.shannonweek14.data.model.Journal
import com.shannon.shannonweek14.data.service.ApiClient
import com.shannon.shannonweek14.data.service.CreateJournalRequest
import com.shannon.shannonweek14.data.service.JournalService

class JournalRepository(private val token: String) {

    private val api = ApiClient.getClient(token).create(JournalService::class.java)

    suspend fun getJournals(): List<Journal> {
        val response = api.getJournals()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Failed: ${response.code()} ${response.message()}")
        }
    }

    suspend fun createJournal(request: CreateJournalRequest): Journal {
        val response = api.createJournal(request)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            throw Exception("Failed: ${response.code()} ${response.message()}")
        }
    }

    suspend fun deleteJournal(id: Int): Boolean {
        val response = api.deleteJournal(id)
        return response.isSuccessful
    }
}
