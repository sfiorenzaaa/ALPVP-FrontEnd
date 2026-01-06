package com.shannon.shannonweek14.repository

import android.util.Log
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.dto.UpdateEventStatusRequest
import com.shannon.shannonweek14.ui.model.Event
import com.shannon.shannonweek14.service.ApiClient
import com.shannon.shannonweek14.service.EventService

class EventRepository(private val token: String? = null) {

    private val api = ApiClient.getClient(token).create(EventService::class.java)

    suspend fun getPublicEvents(): List<Event> {
        val response = api.getPublicEvents()
        if (response.isSuccessful) {
            val list = response.body()?.data ?: emptyList()
            Log.d("DEBUG_REPO", "Public Events Size: ${list.size}") // LOG
            return list
        } else {
            throw Exception("Failed: ${response.code()}")
        }
    }

    // --- BAGIAN INI KITA DEBUG ---
    suspend fun getMyEvents(): List<Event> {
        val response = api.getMyEvents()

        if (response.isSuccessful) {
            val body = response.body()
            Log.d("DEBUG_REPO", "MyEvents Response Body: $body")
            Log.d("DEBUG_REPO", "MyEvents Data Size: ${body?.data?.size}")

            return body?.data ?: emptyList()
        } else {
            val error = response.errorBody()?.string()
            Log.e("DEBUG_REPO", "MyEvents Error: ${response.code()} - $error")
            throw Exception("Failed: ${response.code()}")
        }
    }
    suspend fun createEvent(request: CreateEventRequest): Event {
        val response = api.createEvent(request)
        if (response.isSuccessful) return response.body()?.data!!
        else throw Exception("Failed create")
    }

    suspend fun updateEventStatus(eventId: Int, status: String): Event {
        val response = api.updateEventStatus(eventId, UpdateEventStatusRequest(status))
        if (response.isSuccessful) return response.body()?.data!!
        else throw Exception("Failed update")
    }

    suspend fun getPendingEvents(): List<Event> {
        val response = api.getPendingEvents()
        return if (response.isSuccessful) response.body()?.data ?: emptyList()
        else throw Exception("Failed pending")
    }

    suspend fun joinEvent(token: String, eventId: Int) {
        val formattedToken = "Bearer $token"
        api.joinEvent(formattedToken, eventId)
    }
}