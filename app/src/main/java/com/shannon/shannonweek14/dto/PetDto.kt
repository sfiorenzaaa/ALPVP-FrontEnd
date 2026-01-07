package com.shannon.shannonweek14.dto

import com.google.gson.annotations.SerializedName

data class PetResponseData(
    val id: Int,
    val userId: Int,
    val name: String,
    @SerializedName("healthScore")
    val healthScore: Int,
    @SerializedName("happinessScore")
    val happinessScore: Int,
    @SerializedName("visualState")
    val visualState: String,
    val createdAt: String,
    val updatedAt: String
)

data class CreatePetRequest(
    val name: String
)

data class UpdatePetRequest(
    val name: String
)

data class LogActivityRequest(
    val activityType: String,
    val description: String
)

data class ActivityResponseData(
    val id: Int,
    val userId: Int,
    val activityType: String,
    val description: String,
    val createdAt: String
)
