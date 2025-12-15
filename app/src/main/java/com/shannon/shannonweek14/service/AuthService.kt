package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.data.dto.ApiResponse
import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.LoginResponse
import com.shannon.shannonweek14.data.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("users")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<Any>

    @POST("users/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<LoginResponse>
}
