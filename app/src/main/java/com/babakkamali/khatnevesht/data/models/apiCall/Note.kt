package com.babakkamali.khatnevesht.data.models.apiCall

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("id") val id: Int,
    @SerializedName("note_title") val title: String,
    @SerializedName("note_description") val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class CreateNoteRequest(
    @SerializedName("note_title") val noteTitle: String,
    @SerializedName("note_description") val noteDescription: String,
)


data class CreateNoteResponse(
    @SerializedName("data") val data: Note
)

data class GeneralResponse(
    val status: String,
    val message: String
)

data class SingleNoteResponse(
    val status: String,
    val message: String,
    val data: Note
)

data class AllNotesResponse(
    val data: List<Note>
)