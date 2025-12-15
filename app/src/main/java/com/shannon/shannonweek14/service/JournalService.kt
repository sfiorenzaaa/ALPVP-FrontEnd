package com.shannon.shannonweek14.data.service

import com.shannon.shannonweek14.data.dto.ApiResponse
import com.shannon.shannonweek14.data.model.Journal
import retrofit2.Response
import retrofit2.http.*

data class CreateJournalRequest(
    val content: String
)

interface JournalService {

    @GET("journal")
    suspend fun getJournals(): Response<ApiResponse<List<Journal>>>

    @POST("journal")
    suspend fun createJournal(
        @Body body: CreateJournalRequest
    ): Response<ApiResponse<Journal>>

    @DELETE("journal/{id}")
    suspend fun deleteJournal(
        @Path("id") id: Int
    ): Response<ApiResponse<Any>>
}
