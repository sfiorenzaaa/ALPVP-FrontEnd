package com.shannon.shannonweek14.data.dto

import com.shannon.shannonweek14.data.model.Event

data class CreateEventRequest(
    val title: String,
    val description: String,
    val date: String
)

data class UpdateEventStatusRequest(
    val status: String // "APPROVE" atau "REJECT"
)

data class EventResponse(
    val data: Event
)

data class EventListResponse(
    val data: List<Event>
)