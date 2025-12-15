package com.shannon.shannonweek14.data.repository

import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.RegisterRequest
import com.shannon.shannonweek14.data.service.ApiClient

import com.shannon.shannonweek14.data.service.AuthService

class AuthRepository {

    private val api = ApiClient.getClient().create(AuthService::class.java)

    suspend fun login(request: LoginRequest) =
        api.login(request)


    suspend fun register(request: RegisterRequest) =
        api.register(request)
}
