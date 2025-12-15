package com.shannon.shannonweek14.data.repository

import android.util.Log
import com.shannon.shannonweek14.data.dto.CreateEventRequest
import com.shannon.shannonweek14.data.dto.UpdateEventStatusRequest
import com.shannon.shannonweek14.data.model.Event
import com.shannon.shannonweek14.data.service.ApiClient
import com.shannon.shannonweek14.data.service.EventService // Import ini wajib ada

class EventRepository(private val token: String? = null) {

    private val api = ApiClient.getClient(token).create(EventService::class.java)

    suspend fun getPublicEvents(): List<Event> {
        val response = api.getPublicEvents()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("DEBUG_EVENT", "Get Public Error: $errorBody")
            throw Exception("Failed to fetch events: ${response.code()} $errorBody")
        }
    }

    // --- BAGIAN INI SANGAT PENTING UNTUK CREATE ---
    suspend fun createEvent(request: CreateEventRequest): Event {
        val response = api.createEvent(request)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            // Kita baca pesan error asli dari server (JSON)
            val errorBody = response.errorBody()?.string()

            // Cetak ke Logcat dengan tag "DEBUG_EVENT"
            Log.e("DEBUG_EVENT", "CREATE GAGAL: ${response.code()} - $errorBody")

            throw Exception("Gagal buat event: ${response.code()} $errorBody")
        }
    }

    // Get my events
    suspend fun getMyEvents(): List<Event> {
        val response = api.getMyEvents()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("DEBUG_EVENT", "Get My Events Error: $errorBody")
            throw Exception("Failed to fetch my events: ${response.code()} $errorBody")
        }
    }

    // Admin: Update event status
    suspend fun updateEventStatus(eventId: Int, status: String): Event {
        val response = api.updateEventStatus(eventId, UpdateEventStatusRequest(status))
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Failed to update status: ${response.code()} $errorBody")
        }
    }
}