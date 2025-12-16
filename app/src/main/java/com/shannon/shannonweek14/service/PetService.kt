package com.shannon.shannonweek14.service

import com.shannon.shannonweek14.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PetService {

    @POST("pets")
    suspend fun createPet(
        @Header("Authorization") authHeader: String,
        @Body createPetRequest: CreatePetRequest
    ): Response<ApiResponse<PetResponseData>>

    @GET("pets")
    suspend fun getPetStatus(
        @Header("Authorization") authHeader: String
    ): Response<ApiResponse<PetResponseData>>

    @PUT("pets")
    suspend fun updatePetName(
        @Header("Authorization") authHeader: String,
        @Body updatePetRequest: UpdatePetRequest
    ): Response<ApiResponse<PetResponseData>>

    @POST("pets/activities")
    suspend fun logActivity(
        @Header("Authorization") authHeader: String,
        @Body logActivityRequest: LogActivityRequest
    ): Response<ApiResponse<ActivityResponseData>>

    @GET("pets/activities")
    suspend fun getActivityHistory(
        @Header("Authorization") authHeader: String
    ): Response<ApiResponse<List<ActivityResponseData>>>
}
