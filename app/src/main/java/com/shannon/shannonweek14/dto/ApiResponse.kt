package com.shannon.shannonweek14.dto

data class ApiResponse<T>(
    val message: String,
    val data: T
)
