package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.ApiResponse
import com.shannon.shannonweek14.dto.LoginRequest
import com.shannon.shannonweek14.dto.LoginResponse
import com.shannon.shannonweek14.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("users/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<Any>

    @POST("users/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<LoginResponse>
}
