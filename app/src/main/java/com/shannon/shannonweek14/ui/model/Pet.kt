package com.shannon.shannonweek14.ui.model

data class Pet(
    val id: Int,
    val name: String,
    val healthScore: Int,
    val happinessScore: Int,
    val visualState: String
)

data class PetActivityModel(
    val id: Int,
    val activityType: String,
    val description: String,
    val createdAt: String
)
