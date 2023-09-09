package com.babakkamali.khatnevesht.data.api

import com.babakkamali.khatnevesht.data.models.apiCall.AllNotesResponse
import com.babakkamali.khatnevesht.data.models.apiCall.CreateNoteRequest
import com.babakkamali.khatnevesht.data.models.apiCall.CreateNoteResponse
import com.babakkamali.khatnevesht.data.models.apiCall.GeneralResponse
import com.babakkamali.khatnevesht.data.models.apiCall.LoginRequest
import com.babakkamali.khatnevesht.data.models.apiCall.LoginResponse
import com.babakkamali.khatnevesht.data.models.apiCall.SingleNoteResponse
import com.babakkamali.khatnevesht.data.models.apiCall.VerifyRequest
import com.babakkamali.khatnevesht.data.models.apiCall.VerifyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(@Body requestBody: LoginRequest): Response<LoginResponse>

    @POST("/api/v1/verify/")
    suspend fun verify(@Body requestBody: VerifyRequest): Response<VerifyResponse>

    @GET("api/v1/note/")
    suspend fun getAllNotes(): AllNotesResponse

    @POST("api/v1/note/")
    suspend fun createNote(@Body request: CreateNoteRequest): CreateNoteResponse

    @GET("api/v1/note/{id}")
    suspend fun getNoteById(@Path("id") id: Int): SingleNoteResponse

    @PATCH("api/v1/note/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body request: CreateNoteRequest): GeneralResponse

    @DELETE("api/v1/note/{id}")
    suspend fun deleteNote(@Path("id") id: Int): GeneralResponse
}
