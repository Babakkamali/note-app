package com.babakkamali.khatnevesht.data.models.apiCall

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("phone_number") val phoneNumber: String
)

data class LoginResponse(
    val status: String,
    val message: String
)