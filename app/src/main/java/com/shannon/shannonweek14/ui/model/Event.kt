package com.shannon.shannonweek14.ui.model

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val eventDate: String,
    val status: String, // "PENDING", "APPROVE", "REJECT"
    val createdAt: String,
    val userId: Int,
    val user: EventUser? = null,
    val date: String?,
    val author: String?,
    val isJoined: Boolean = false
)

data class EventUser(
    val username: String
)