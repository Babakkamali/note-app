package com.babakkamali.khatnevesht.domain.authentication

import com.babakkamali.khatnevesht.data.api.ApiClient
import com.babakkamali.khatnevesht.data.models.apiCall.LoginRequest
import com.babakkamali.khatnevesht.data.models.apiCall.LoginResponse
import com.babakkamali.khatnevesht.data.models.apiCall.VerifyRequest
import com.babakkamali.khatnevesht.data.models.apiCall.VerifyResponse
import com.google.gson.Gson

class AuthenticationRepository {
    private val apiService = ApiClient.apiService
    private val gson = Gson()

    suspend fun loginUser(phoneNumber: String): LoginResponse? {
        val request = LoginRequest(phoneNumber)
        val response = apiService.login(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            gson.fromJson(response.errorBody()?.string(), LoginResponse::class.java)
        }
    }

    suspend fun verifyUser(phoneNumber: String, token: String): VerifyResponse? {
        val request = VerifyRequest(phoneNumber, token)
        val response = apiService.verify(request)
        return if (response.isSuccessful) {
            response.body()
        } else {
            gson.fromJson(response.errorBody()?.string(), VerifyResponse::class.java)
        }
    }
}