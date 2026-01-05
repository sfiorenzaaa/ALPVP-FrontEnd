package com.shannon.shannonweek14.dto

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)