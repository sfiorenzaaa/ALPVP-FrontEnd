package com.shannon.shannonweek14.dto

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("author")
    val author: String? = null,

    @SerializedName("status")
    val status: String? = null
)