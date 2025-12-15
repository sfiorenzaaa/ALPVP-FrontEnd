package com.shannon.shannonweek14.repository

import com.shannon.shannonweek14.dto.ApiResponse
import com.shannon.shannonweek14.dto.LoginRequest
import com.shannon.shannonweek14.dto.LoginResponse
import com.shannon.shannonweek14.dto.RegisterRequest
import com.shannon.shannonweek14.service.ApiClient
import com.shannon.shannonweek14.service.AuthService

class AuthRepository {

    private val api = ApiClient.getClient().create(AuthService::class.java)

    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return api.login(request)
    }

    suspend fun register(registerRequest: RegisterRequest): ApiResponse<Any> {
        return api.register(registerRequest)
    }
}