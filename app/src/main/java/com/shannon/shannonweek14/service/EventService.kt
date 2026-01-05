package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
import com.shannon.shannonweek14.ui.model.Event
import retrofit2.Response
import retrofit2.http.*

interface EventService {

    @GET("events")
    suspend fun getPublicEvents(): Response<ApiResponse<List<Event>>>

    @GET("events/pending")
    suspend fun getPendingEvents(): Response<ApiResponse<List<Event>>>


    @POST("events")
    suspend fun createEvent(
        @Body request: CreateEventRequest
    ): Response<ApiResponse<Event>>

    @GET("events/my")
    suspend fun getMyEvents(): Response<ApiResponse<List<Event>>>


    @PATCH("events/{id}")
    suspend fun updateEventStatus(
        @Path("id") eventId: Int,
        @Body request: UpdateEventStatusRequest
    ): Response<ApiResponse<Event>>

    @POST("events/{id}/join")
    suspend fun joinEvent(
        @Header("Authorization") token: String,
        @Path("id") eventId: Int
    ): Response<Void>
}