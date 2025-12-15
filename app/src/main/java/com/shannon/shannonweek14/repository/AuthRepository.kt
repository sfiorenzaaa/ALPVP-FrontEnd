package com.shannon.shannonweek14.data.repository

import com.shannon.shannonweek14.data.dto.ApiResponse
import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.LoginResponse
import com.shannon.shannonweek14.data.dto.RegisterRequest
import com.shannon.shannonweek14.data.service.ApiClient
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import com.shannon.shannonweek14.data.service.AuthService
=======

import com.shannon.shannonweek14.service.AuthService
>>>>>>> Stashed changes
=======

import com.shannon.shannonweek14.service.AuthService
>>>>>>> Stashed changes
=======

import com.shannon.shannonweek14.service.AuthService
>>>>>>> Stashed changes
=======

import com.shannon.shannonweek14.service.AuthService
>>>>>>> Stashed changes

class AuthRepository {

    private val api = ApiClient.getClient().create(AuthService::class.java)

    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return api.login(request)
    }
    suspend fun register(registerRequest: RegisterRequest): ApiResponse<Any> {
        return api.register(registerRequest)
    }
}
