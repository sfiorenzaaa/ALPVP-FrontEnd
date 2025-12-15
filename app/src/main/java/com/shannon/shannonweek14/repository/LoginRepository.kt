package com.shannon.shannonweek14.repository


import com.shannon.shannonweek14.data.datastore.TokenManager
import com.shannon.shannonweek14.data.dto.ApiResponse
import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.LoginResponse
import com.shannon.shannonweek14.service.AuthService


class LoginRepository(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) {

    suspend fun login(email: String, password: String): LoginResponse {

        val response: ApiResponse<LoginResponse> =
            authService.login(LoginRequest(email, password))

        if (response.message.contains("success", ignoreCase = true)) {
            val token = response.data.token
            tokenManager.saveToken(token)
            return response.data
        } else {
            throw Exception(response.message)
        }
    }
}
