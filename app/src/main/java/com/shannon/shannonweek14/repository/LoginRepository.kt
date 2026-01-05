package com.shannon.shannonweek14.repository

import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.dto.ApiResponse
import com.shannon.shannonweek14.dto.LoginRequest
import com.shannon.shannonweek14.dto.LoginResponse
import com.shannon.shannonweek14.service.AuthService

class LoginRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): LoginResponse {

        val response: ApiResponse<LoginResponse> =
            authService.login(LoginRequest(email, password))

        val loginData = response.data
        val message = response.message ?: ""

        if (loginData != null && message.contains("success", ignoreCase = true)) {

            tokenManager.saveToken(loginData.token)

            return loginData
        } else {
            throw Exception(message.ifEmpty { "Login failed or data is empty" })
        }
    }
}