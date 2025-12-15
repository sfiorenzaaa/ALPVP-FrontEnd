package com.shannon.shannonweek14.data.repository

<<<<<<< Updated upstream
import com.shannon.shannonweek14.data.dto.CreateEventRequest
import com.shannon.shannonweek14.data.dto.UpdateEventStatusRequest
import com.shannon.shannonweek14.data.model.Event
import com.shannon.shannonweek14.data.service.ApiClient
import com.shannon.shannonweek14.data.service.EventService
=======
import com.shannon.shannonweek14.dto.CreateEventRequest
import com.shannon.shannonweek14.dto.UpdateEventStatusRequest
import com.shannon.shannonweek14.ui.model.Event
import com.shannon.shannonweek14.data.service.ApiClient
import com.shannon.shannonweek14.service.EventService
>>>>>>> Stashed changes

class EventRepository(private val token: String? = null) {

    private val api = ApiClient.getClient(token).create(EventService::class.java)

    suspend fun getPublicEvents(): List<Event> {
        val response = api.getPublicEvents()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Failed to fetch events: ${response.code()} ${response.message()}")
        }
    }

    suspend fun createEvent(request: CreateEventRequest): Event {
        val response = api.createEvent(request)
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            throw Exception("Failed to create event: ${response.code()} ${response.message()}")
        }
    }

<<<<<<< Updated upstream
    // Get my events
=======
>>>>>>> Stashed changes
    suspend fun getMyEvents(): List<Event> {
        val response = api.getMyEvents()
        if (response.isSuccessful) {
            return response.body()?.data ?: emptyList()
        } else {
            throw Exception("Failed to fetch my events: ${response.code()} ${response.message()}")
        }
    }

<<<<<<< Updated upstream
    // Admin: Update event status
=======
>>>>>>> Stashed changes
    suspend fun updateEventStatus(eventId: Int, status: String): Event {
        val response = api.updateEventStatus(eventId, UpdateEventStatusRequest(status))
        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            throw Exception("Failed to update status: ${response.code()} ${response.message()}")
        }
    }
}