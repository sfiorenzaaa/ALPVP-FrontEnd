package com.shannon.shannonweek14.repository

import com.shannon.shannonweek14.dto.CreatePetRequest
import com.shannon.shannonweek14.dto.LogActivityRequest
import com.shannon.shannonweek14.dto.UpdatePetRequest
import com.shannon.shannonweek14.service.PetService

class PetRepository(private val petService: PetService, private val token: String) {

    private val authHeader by lazy { "Bearer $token" }

    suspend fun createPet(name: String) = petService.createPet(authHeader, CreatePetRequest(name))

    suspend fun getPetStatus() = petService.getPetStatus(authHeader)

    suspend fun updatePetName(name: String) = petService.updatePetName(authHeader, UpdatePetRequest(name))

    suspend fun logActivity(activityType: String, description: String) =
        petService.logActivity(authHeader, LogActivityRequest(activityType, description))

    suspend fun getActivityHistory() = petService.getActivityHistory(authHeader)
}
