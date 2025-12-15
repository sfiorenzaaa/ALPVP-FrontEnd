package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
import com.shannon.shannonweek14.ui.model.Event
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    // Public
    @GET("events")
    suspend fun getPublicEvents(): Response<EventListResponse>

    @GET("events/pending")
    suspend fun getPendingEvents(): Response<ApiResponse<List<Event>>>


    // Private
    @POST("events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): Response<EventResponse>

    // Private
    @GET("events/my")
    suspend fun getMyEvents(): Response<EventListResponse>

    // Admin only
    @PATCH("events/{id}")
    suspend fun updateEventStatus(
        @Path("id") eventId: Int,
        @Body request: UpdateEventStatusRequest
    ): Response<EventResponse>

    data class StatusUpdateReq(
        val status: String
    )

}