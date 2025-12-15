package com.shannon.shannonweek14.data.dto

data class ApiResponse<T>(
    val message: String,
    val data: T
)
