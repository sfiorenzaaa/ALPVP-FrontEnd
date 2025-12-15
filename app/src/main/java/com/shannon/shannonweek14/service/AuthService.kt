package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.data.dto.ApiResponse
import com.shannon.shannonweek14.data.dto.LoginRequest
import com.shannon.shannonweek14.data.dto.LoginResponse
import com.shannon.shannonweek14.data.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    @POST("users/register")
=======
    @POST("users")
>>>>>>> Stashed changes
=======
    @POST("users")
>>>>>>> Stashed changes
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<Any>

    @POST("users/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<LoginResponse>
}
