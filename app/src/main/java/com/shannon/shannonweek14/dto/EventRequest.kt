package com.shannon.shannonweek14.dto

import com.shannon.shannonweek14.ui.model.Event

data class CreateEventRequest(
    val title: String,
    val description: String,
    val date: String
)

data class UpdateEventStatusRequest(
    val status: String // "APPROVE" atau "REJECT"
)