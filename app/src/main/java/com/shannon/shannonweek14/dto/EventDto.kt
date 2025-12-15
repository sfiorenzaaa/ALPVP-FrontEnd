package com.shannon.shannonweek14.dto

import com.shannon.shannonweek14.ui.model.Event

data class CreateEventRequest(
    val title: String,
    val description: String,
    val date: String // Format: "2025-12-31T10:00:00Z"
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