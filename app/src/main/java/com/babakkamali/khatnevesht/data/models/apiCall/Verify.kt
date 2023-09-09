package com.babakkamali.khatnevesht.data.models.apiCall

import com.google.gson.annotations.SerializedName

data class VerifyRequest(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("sms_token") val smsToken: String
)

// response
data class VerifyResponse(
    val status: String,
    val message: String,
    val data: VerifyData
)

data class VerifyData(
    val token: String
)