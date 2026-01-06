package com.shannon.shannonweek14.dto

import com.google.gson.annotations.SerializedName
data class LoginResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("id")
    val id: Int


)

data class UserData(
    @SerializedName("token")
    val token: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String
)